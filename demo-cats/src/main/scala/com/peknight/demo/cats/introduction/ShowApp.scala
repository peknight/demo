package com.peknight.demo.cats.introduction

object ShowApp extends App {
  import cats.Show

  // 2.2.0版本后的cats不再需要`import cats.instances`，见https://github.com/typelevel/cats/releases/tag/v2.2.0
//  import cats.instances.int._
//  import cats.instances.string._

  val showInt = Show.apply[Int]
  val showString = Show.apply[String]

  val intAsString: String = showInt.show(123)
  println(intAsString)
  val stringAsString: String = showString.show("abc")
  println(stringAsString)

  import cats.syntax.show._
  println(123.show)
  println("abc".show)

  import java.util.Date
  val dateShow: Show[Date] = (date: Date) => s"${date.getTime}ms since the epoch."
  val dataShow1: Show[Date] = Show.show(date => s"${date.getTime}ms since the epoch.")
  implicit val dataShow2: Show[Date] = Show.fromToString

  println(new Date().show)

  import ShowInstances._
  println(Cat("Java", 3, "yellow").show)
}
