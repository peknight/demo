package com.peknight.demo.cats.introduction

import cats.Show
import cats.syntax.show.*
import com.peknight.demo.cats.introduction.ShowInstances.given

import java.util.Date

object ShowApp extends App:

  // 2.2.0版本后的cats不再需要`import cats.instances`，见https://github.com/typelevel/cats/releases/tag/v2.2.0
//  import cats.instances.int._
//  import cats.instances.string._

  val showInt = Show.apply[Int]
  val showString = Show.apply[String]

  val intAsString: String = showInt.show(123)
  println(intAsString)
  val stringAsString: String = showString.show("abc")
  println(stringAsString)

  println(123.show)
  println("abc".show)

  val dateShow: Show[Date] = (date: Date) => s"${date.getTime}ms since the epoch."
  val dataShow1: Show[Date] = Show.show(date => s"${date.getTime}ms since the epoch.")
  given Show[Date] = Show.fromToString

  println(new Date().show)

  println(Cat("Java", 3, "yellow").show)
