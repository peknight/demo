package com.peknight.demo.cats.monad

import cats.Monad
import cats.syntax.applicative._

object OptionApp extends App {
  def parseInt(str: String): Option[Int] = scala.util.Try(str.toInt).toOption

  def divide(a: Int, b: Int): Option[Int] = if (b == 0) None else Some(a / b)

  def stringDivideBy(aStr: String, bStr: String): Option[Int] = for {
    aNum <- parseInt(aStr)
    bNum <- parseInt(bStr)
    ans <- divide(aNum, bNum)
  } yield ans

  println(stringDivideBy("6", "2"))
  println(stringDivideBy("6", "0"))
  println(stringDivideBy("6", "foo"))
  println(stringDivideBy("bar", "2"))


  val opt1 = Monad[Option].pure(3)
  println(opt1)
  val opt2 = Monad[Option].flatMap(opt1)(a => Some(a + 2))
  println(opt2)
  val opt3 = Monad[Option].map(opt2)(a => 100 * a)
  println(opt3)

  1.pure[Option]
}
