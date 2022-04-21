package com.peknight.demo.fpinscala.iomonad

import com.peknight.demo.fpinscala.iomonad.Free.{Suspend, runFree, runTrampoline, translate}
import com.peknight.demo.fpinscala.iomonad.Translate.~>
import com.peknight.demo.fpinscala.monads.Monad
import com.peknight.demo.fpinscala.monads.Monad.parMonad
import com.peknight.demo.fpinscala.parallelism.Nonblocking.Par

import scala.io.StdIn.readLine

sealed trait Console[A] {
  def toPar: Par[A]
  def toThunk: () => A
  def toState: ConsoleState[A]
  def toReader: ConsoleReader[A]
}
object Console {
  case object ReadLine extends Console[Option[String]] {
    def toPar = Par.lazyUnit(run)
    def toThunk = () => run

    def run: Option[String] = try Option(readLine()) catch { case e: Exception => None }

    def toState = ConsoleState {
      case Buffers(head :: tail, out) => (Some(head), Buffers(tail, out))
      case buffers @ Buffers(_, _) => (None, buffers)
    }
    def toReader = ConsoleReader { in => Some(in) }
  }
  case class PrintLine(line: String) extends Console[Unit] {
    def toPar = Par.lazyUnit(println(line))
    def toThunk = () => println(line)

    def toState = ConsoleState(buffers => ((), Buffers(buffers.in, buffers.out :+ line)))
    def toReader = ConsoleReader { s => () }
  }

  type ConsoleIO[A] = Free[Console, A]
  def readLn: ConsoleIO[Option[String]] = Suspend(ReadLine)
  def printLn(line: String): ConsoleIO[Unit] = Suspend(PrintLine(line))

  val consoleToFunction0 = new (Console ~> Function0) {
    def apply[A](a: Console[A]) = a.toThunk
  }
  val consoleToPar = new (Console ~> Par) {
    def apply[A](a: Console[A]) = a.toPar
  }

  given function0Monad: Monad[Function0] with
    def unit[A](a: => A) = () => a
    override def flatMap[A, B](a: Function0[A])(f: A => Function0[B]) = () => f(a())()

  def runConsoleFunction0[A](a: Free[Console, A]): () => A = runFree[Console, Function0, A](a)(consoleToFunction0)
  def runConsolePar[A](a: Free[Console, A]): Par[A] = runFree[Console, Par, A](a)(consoleToPar)(parMonad)

  def runConsole[A](a: Free[Console, A]): A = runTrampoline { translate(a)(consoleToFunction0) }

  val consoleToReader = new (Console ~> ConsoleReader) {
    def apply[A](a: Console[A]) = a.toReader
  }

  val consoleToState = new (Console ~> ConsoleState) {
    def apply[A](a: Console[A]) = a.toState
  }

  /*
   * runConsoleReader and runConsoleState aren't stack-safe as implemented,
   * for the same reason that runConsoleFunction0 wasn't stack-safe.
   * We can fix this by changing the representations to String => TailRec[A] for ConsoleReader
   * and Buffers => TailRec[(A, Buffers)] for ConsoleState.
   */
  def runConsoleReader[A](io: ConsoleIO[A]): ConsoleReader[A] = runFree[Console, ConsoleReader, A](io)(consoleToReader)
  def runConsoleState[A](io: ConsoleIO[A]): ConsoleState[A] = runFree[Console, ConsoleState, A](io)(consoleToState)
}
