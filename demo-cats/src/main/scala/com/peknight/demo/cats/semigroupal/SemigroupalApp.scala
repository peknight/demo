package com.peknight.demo.cats.semigroupal

import cats.Semigroupal
import cats.syntax.either._

object SemigroupalApp extends App {
  def parseInt(str: String): Either[String, Int] =
    Either.catchOnly[NumberFormatException](str.toInt).leftMap(_ => s"Couldn't read $str")

  val eitherRes = for {
    a <- parseInt("a")
    b <- parseInt("b")
    c <- parseInt("c")
  } yield (a + b + c)
  println(eitherRes)

  println(Semigroupal[Option].product(Some(123), Some("abc")))
  println(Semigroupal[Option].product(None, Some("abc")))
  println(Semigroupal[Option].product(Some("abc"), None))

  // Joining Three or More Contexts


  println(Semigroupal.tuple3(Option(1), Option(2), Option(3)))
  println(Semigroupal.tuple3(Option(1), Option(2), Option.empty[Int]))

  Semigroupal.map3(Option(1), Option(2), Option(3))(_ + _ + _)
  Semigroupal.map2(Option(1), Option.empty[Int])(_ + _)
}
