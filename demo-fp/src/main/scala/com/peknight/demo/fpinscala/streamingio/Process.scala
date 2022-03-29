package com.peknight.demo.fpinscala.streamingio

import com.peknight.demo.fpinscala.iomonad.App.{IO, unsafePerformIO}
import com.peknight.demo.fpinscala.iomonad.Free.Suspend
import com.peknight.demo.fpinscala.parallelism.Nonblocking.Par

import java.util.concurrent.Executors

/**
 * GeneralizeStreamTransducers
 */
trait Process[F[_], O] {
  import Process._

  def map[O2](f: O => O2): Process[F, O2] = this match {
    case Await(req, recv) => Await(req, recv andThen (_.map(f)))
    case Emit(h, t) => Try { Emit(f(h), t map f)}
    case Halt(err) => Halt(err)
  }

  def onHalt(f: Throwable => Process[F, O]): Process[F, O] = this match {
    case Halt(e) => Try(f(e))
    case Emit(h, t) => Emit(h, t.onHalt(f))
    case Await(req, recv) => Await(req, recv.andThen(_.onHalt(f)))
  }

  def ++(p: => Process[F, O]): Process[F, O] = this.onHalt {
    case End => p
    case err => Halt(err)
  }

  def flatMap[O2](f: O => Process[F, O2]): Process[F, O2] = this match {
    case Halt(err) => Halt(err)
    case Emit(o, t) => Try(f(o)) ++ t.flatMap(f)
    case Await(req, recv) => Await(req, recv.andThen(_.flatMap(f)))
  }

  // Exercise 15.10
  def runLog(implicit F: MonadCatch[F]): F[IndexedSeq[O]] = {
    def go(cur: Process[F, O], acc: IndexedSeq[O]): F[IndexedSeq[O]] = cur match {
      case Emit(h, t) => go(t, acc :+ h)
      case Halt(End) => F.unit(acc)
      case Halt(err) => F.fail(err)
      // 这里的实现是没法进行尾递归优化的，完全依赖底层的monad来保证栈安全
      case Await(req, recv) => F.flatMap(F.attempt(req)) {e => go(Try(recv(e)), acc)}
    }
    go(this, IndexedSeq())
  }
}
object Process {
  // recv函数应该是以trampolined的方式返回TailRec[Process[F, O]]，从而避免栈溢出的问题，但为了保持简单，这里并未这么做
  case class Await[F[_], A, O](req: F[A], recv: Either[Throwable, A] => Process[F, O]) extends Process[F, O]
  case class Emit[F[_], O](head: O, tail: Process[F, O]) extends Process[F, O]
  // Halt due to err, which could be an actual error or End indicating normal termination.
  case class Halt[F[_], O](err: Throwable) extends Process[F, O]

  case object End extends Exception
  case object Kill extends Exception

  def await[F[_], A, O](req: F[A])(recv: Either[Throwable, A] => Process[F, O]): Process[F, O] = Await(req, recv)

  /* Helper function to safely produce p, or gracefully halt with an error if an exception is thrown. */
  def Try[F[_], O](p: => Process[F, O]): Process[F, O] = try p catch { case e: Throwable => Halt(e) }

  def runLog[O](src: Process[IO, O]): IO[IndexedSeq[O]] = Suspend(Par.lazyUnit {
    val E = Executors.newFixedThreadPool(4)
    def go(cur: Process[IO, O], acc: IndexedSeq[O]): IndexedSeq[O] = cur match {
      case Emit(h, t) => go(t, acc :+ h)
      case Halt(End) => acc
      case Halt(err) => throw err
      case Await(req, recv) => {
        val next = try recv(Right(unsafePerformIO(req)(E))) catch { case err: Throwable => recv(Left(err)) }
        go(next, acc)
      }
    }
    try go(src, IndexedSeq()) finally E.shutdown()
  })
}
