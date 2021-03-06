package com.peknight.demo.cats.monad

import cats.{Id, Monad}
import cats.syntax.flatMap._
import cats.syntax.functor._

object MonadApp extends App {

  def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
    for {
      x <- a
      y <- b
    } yield x * x + y * y

  println(sumSquare(Option(3), Option(4)))
  println(sumSquare(List(1, 2, 3), List(4, 5)))
  println(sumSquare(3: Id[Int], 4: Id[Int]))
}
