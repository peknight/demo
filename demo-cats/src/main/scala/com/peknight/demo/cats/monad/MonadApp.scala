package com.peknight.demo.cats.monad

import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.monad._
import cats.{Id, Monad}
import com.peknight.demo.cats.functor.Tree
import com.peknight.demo.cats.functor.Tree._
import MonadInstances._

object MonadApp extends App {

  def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
    for {
      x <- a
      y <- b
    } yield x * x + y * y

  println(sumSquare(Option(3), Option(4)))
  println(sumSquare(List(1, 2, 3), List(4, 5)))
  println(sumSquare(3: Id[Int], 4: Id[Int]))

  def retry[F[_]: Monad, A](start: A)(f: A => F[A]): F[A] =
    f(start).flatMap { a =>
      retry(a)(f)
    }

  def retryTailRecM[F[_]: Monad, A](start: A)(f: A => F[A]): F[A] =
    Monad[F].tailRecM(start) { a =>
      f(a).map(a2 => Left(a2))
    }

  def retryM[F[_]: Monad, A](start: A)(f: A => F[A]): F[A] = start.iterateWhileM(f)(_ => true)

  println(retry(100)(a => if (a == 0) Option.empty[Int] else Option(a - 1)))
  println(retryTailRecM(100000)(a => if (a == 0) Option.empty[Int] else Option(a - 1)))
  println(retryM(100000)(a => if (a == 0) Option.empty[Int] else Option(a - 1)))

  println(branch(leaf(100), leaf(200)).flatMap(x =>
    branch(leaf(x - 1), leaf(x + 1))
  ))

  val treeFor = for {
    a <- branch(leaf(100), leaf(200))
    b <- branch(leaf(a - 10), leaf(a + 10))
    c <- branch(leaf(b - 1), leaf(b + 1))
  } yield c
  println(treeFor)


}
