package com.peknight.demo.cats.introduction

import cats.Eq
import cats.syntax.eq.*
import cats.syntax.option.*
import com.peknight.demo.cats.introduction.EqInstances.given

import java.util.Date

object EqApp extends App:
  val eqInt = Eq[Int]
  println(eqInt.eqv(123, 123))
  println(eqInt.eqv(123, 234))
  println(123 === 123)

  println(1.some === none[Int])
  println(1.some =!= none[Int])

  given Eq[Date] = Eq.instance[Date] { (date1, date2) =>
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

  println(cat1 === cat2)
  println(cat1 =!= cat2)

  println(optionCat1 === optionCat2)
  println(optionCat1 =!= optionCat2)
