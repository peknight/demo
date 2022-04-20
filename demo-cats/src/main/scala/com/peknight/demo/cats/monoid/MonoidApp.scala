package com.peknight.demo.cats.monoid

import cats.{Monoid, Semigroup}
import cats.syntax.semigroup.*

object MonoidApp extends App:
//  import com.peknight.demo.cats.monoid.MonoidInstances.{intSetMonoid, strSetMonoid}
//  println(intSetMonoid.combine(Set(1, 2), Set(2, 3)))
//  println(strSetMonoid.combine(Set("A", "B"), Set("B", "C")))
  println(Monoid[String].combine("Hi ", "there"))
  Monoid[String].empty

  println(Semigroup[String].combine("Hi ", "there"))

  println(Monoid[Int].combine(32, 10))

  val a = Option(22)
  val b = Option(20)
  println(Monoid[Option[Int]].combine(a, b))
  println(Monoid[Option[Int]].combine(a, Option.empty[Int]))


  val intResult = 1 |+| 2 |+| Monoid[Int].empty
  println(intResult)

//  def add(items: List[Int]): Int = {
//    items.foldLeft(Monoid[Int].empty)(_ |+| _)
//  }


//  def add[A](items: Seq[A])(using m: Monoid[A]): A = {
//    items.foldLeft(m.empty)(_ |+| _)
//  }

  def add[A: Monoid](items: List[A]): A = items.foldLeft(Monoid[A].empty)(_ |+| _)

  println(add(List(1, 2, 3, 4, 5)))
  println(add(List(Option(1), Option(2), Option(3), Option.empty)))
