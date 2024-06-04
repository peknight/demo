package com.peknight.demo.fpinscala.iomonad

import com.peknight.demo.fpinscala.iomonad.IO.{FlatMap, Return}
import com.peknight.demo.fpinscala.monads.Monad

import scala.annotation.tailrec
import scala.io.StdIn.readLine
import scala.language.implicitConversions

sealed trait IO[A]:
  self =>

  def flatMap[B](f: A => IO[B]): IO[B] = FlatMap(this, f)

  def map[B](f: A => B): IO[B] = flatMap(f andThen (Return(_)))

//  def run: A

//  def map[B](f: A => B): IO[B] = new IO:
//    def run = f(self.run)

//  def flatMap[B](f: A => IO[B]): IO[B] = new IO:
//    def run = f(self.run).run

//  def ++(io: IO) = new IO:
//    def run =
//      self.run
//      io.run

end IO

object IO extends Monad[IO]:

  case class Return[A](a: A) extends IO[A]
  case class Suspend[A](resume: () => A) extends IO[A]
  case class FlatMap[A, B](sub: IO[A], k: A => IO[B]) extends IO[B]

  @tailrec def run[A](io: IO[A]): A = io match
    case Return(a) => a
    case Suspend(r) => r()
    case FlatMap(x, f) => x match
      // FlatMap有两个泛型参数，但是run方法只指定了一个，所以idea推断不出来左侧泛型的类型而报红，实际是可以编译通过的
      case Return(a) => run(f(a))
      // 无限调用会走到这里，先执行r()，也就是实际要执行的业务代码，将执行结果做为参数来执行f函数产生新的IO对象，然后尾递归继续run这个新IO对象
      case Suspend(r) => run(f(r()))
      /*
       * io @ FlatMap(x @ FlatMap(y, g), f)
       * y.flatMap(g).flatMap(f)
       * y.flatMap(a => g(a).flatMap(f)) // Monad Associativity Law
       * y.flatMap(a => FlatMap(g(a), f))
       * FlatMap(y, a => FlatMap(g(a), f))
       */
      case FlatMap(y, g) => run(y.flatMap(a => g(a).flatMap(f)))

  def unit[A](a: => A): IO[A] = Suspend(() => a)
  // new IO[A] { def run = a }

  override def flatMap[A, B](fa: IO[A])(f: A => IO[B]) = fa.flatMap(f)

  def apply[A](a: => A): IO[A] = unit(a)

  def ref[A](a: A): IO[IORef[A]] = IO { new IORef(a) }

  def ReadLine: IO[String] = IO { readLine() }

//  def PrintLine(msg: String): IO[Unit] = IO { println(msg) }
  def PrintLine(s: String): IO[Unit] = Suspend(() => Return(println(s)))

  def empty: IO[Unit] = IO { () }

  val echo = ReadLine.flatMap(PrintLine)
  val readInt = ReadLine.map(_.toInt)
  val readInts = readInt ** readInt
  val readLine10 = replicateM(10)(ReadLine)

end IO
