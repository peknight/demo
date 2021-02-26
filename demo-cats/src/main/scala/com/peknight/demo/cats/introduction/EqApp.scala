package com.peknight.demo.cats.introduction

object EqApp extends App {
  import cats.Eq
  val eqInt = Eq[Int]
  println(eqInt.eqv(123, 123))
  println(eqInt.eqv(123, 234))
  import cats.syntax.eq._
  println(123 === 123)

  import cats.syntax.option._
  println(1.some === none[Int])
  println(1.some =!= none[Int])

  import java.util.Date
  implicit val dateEq: Eq[Date] = Eq.instance[Date] { (date1, date2) =>
    date1.getTime === date2.getTime
  }
  val now = System.currentTimeMillis()
  val x = new Date(now)
  val y = new Date(now + 1)
  println(x === x)
  println(x === y)

  val cat1 = Cat("Garfield", 38, "orange and black")
  val cat2 = Cat("Heathcliff", 33, "orange and black")

  val optionCat1 = cat1.some
  val optionCat2 = none[Cat]

  import EqInstances._
  println(cat1 === cat2)
  println(cat1 =!= cat2)

  println(optionCat1 === optionCat2)
  println(optionCat1 =!= optionCat2)
}
