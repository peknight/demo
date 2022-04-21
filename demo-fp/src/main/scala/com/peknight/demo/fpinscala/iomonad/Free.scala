package com.peknight.demo.fpinscala.iomonad

import com.peknight.demo.fpinscala.iomonad.Free.{FlatMap, Return}
import com.peknight.demo.fpinscala.iomonad.Translate.~>
import com.peknight.demo.fpinscala.monads.Monad
import com.peknight.demo.fpinscala.parallelism.Nonblocking.Par

import scala.annotation.tailrec

sealed trait Free[F[_], A]:
  def flatMap[B](f: A => Free[F, B]): Free[F, B] = FlatMap(this, f)
  def map[B](f: A => B): Free[F, B] = flatMap(a => Return[F, B](f(a)))

object Free:
  case class Return[F[_], A](a: A) extends Free[F, A]
  case class Suspend[F[_], A](s: F[A]) extends Free[F, A]
  case class FlatMap[F[_], A, B](s: Free[F, A], f: A => Free[F, B]) extends Free[F, B]

  // Exercise 13.1

  given freeMonad[F[_]]: Monad[[T] =>> Free[F, T]] with
    def unit[A](a: => A): Free[F, A] = Return(a)
    override def flatMap[A, B](fa: Free[F, A])(f: A => Free[F, B]): Free[F, B] = fa.flatMap(f)

  type TailRec[A] = Free[Function0, A]
  type Async[A] = Free[Par, A]

  // Exercise 13.2

  @tailrec def runTrampoline[A](a: Free[Function0, A]): A = a match
    case Return(a) => a
    case Suspend(s) => s()
    case FlatMap(x, f) => x match
      case Return(a) => runTrampoline(f(a))
      case Suspend(s) => runTrampoline(f(s()))
      case FlatMap(y, g) => runTrampoline(y flatMap (a => g(a) flatMap f))

  // Exercise 13.3

  //noinspection DuplicatedCode
  @tailrec def step[F[_], A](free: Free[F, A]): Free[F, A] = free match
    case FlatMap(FlatMap(x, f), g) => step(x flatMap (a => f(a) flatMap g))
    case FlatMap(Return(x), f) => step(f(x))
    case _ => free

  def run[F[_], A](a: Free[F, A])(using F: Monad[F]): F[A] = step(a) match
    case Return(a) => F.unit(a)
    case Suspend(s) => s
    case FlatMap(Suspend(s), f) => F.flatMap(s)(a => run(f(a)))
    case _ => sys.error("Impossible, since `step` eliminates these cases")

  def runFree[F[_], G[_], A](free: Free[F, A])(t: F ~> G)(using G: Monad[G]): G[A] = step(free) match
    case Return(a) => G.unit(a)
    case Suspend(r) => t(r)
    case FlatMap(Suspend(r), f) => G.flatMap(t(r))(a => runFree(f(a))(t))
    case _ => sys.error("Impossible; `step` eliminates these cases")

  // Exercise 13.4

  def translate[F[_], G[_], A](free: Free[F, A])(fg: F ~> G): Free[G, A] =
    type FreeG[A] = Free[G, A]
    val t = new (F ~> FreeG):
      def apply[A](a: F[A]): Free[G, A] = Suspend { fg(a) }
    runFree(free)(t)

end Free
