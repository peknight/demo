package com.peknight.demo.fpinscala.errorhandling

import com.peknight.demo.fpinscala.errorhandling.Option._

object ErrorHandlingApp extends App {
  def failingFn(i: Int): Int = {
    val y: Int = throw new Exception("fail!")
    try {
      val x = 42 + 5
      x + y
    } catch {
      case e: Exception => 43
    }
  }

  def failingFn2(i: Int): Int = {
    try {
      val x = 42 + 5
      x + ((throw new Exception("fail!")): Int)
    } catch {
      case e: Exception => 43
    }
  }

  println(failingFn2(12))

  def mean(xs: Seq[Double]): Double =
    if (xs.isEmpty) throw new ArithmeticException("mean of empty list!")
    else xs.sum / xs.length

  def meanWithOption(xs: Seq[Double]): Option[Double] =
    if (xs.isEmpty) None
    else Some(xs.sum / xs.length)

  def lookupByName(name: String): Option[Employee] =
    Map("Joe" -> Employee("Joe", "Accounting", None)).get(name) match {
      case scala.Some(e) => Some(e)
      case scala.None => None
    }

  val joeDepartment: Option[String] = lookupByName("Joe").map(_.department)

  lookupByName("Joe").flatMap(_.manager)
  lookupByName("Joe").map(_.department).getOrElse("Default Dept. ")

  // Exercise 4.2
  def variance(xs: Seq[Double]): Option[Double] =
    meanWithOption(xs).flatMap(m => meanWithOption(xs.map(x => math.pow(x - m, 2))))

  val dept: String = lookupByName("Joe")
    .map(_.department)
    .filter(_ != "Accounting")
    .getOrElse("Default Dept")

}
