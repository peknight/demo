package com.peknight.demo.scala3.givens

import com.peknight.demo.scala3.functionalobjects.Rational
import com.peknight.demo.scala3.givens.Ord.*

import scala.concurrent.Future


object GivensApp extends App:
  {
    import com.peknight.demo.scala3.givens.JillsPrefs.{drink, prompt}
    Greeter.greet("Jill")
    println(implicitly[PreferredPrompt].preference)
  }
  {
    import com.peknight.demo.scala3.givens.TomsPrefs.given
    Greeter.greet("Tom")
  }
  {
    import com.peknight.demo.scala3.givens.TomsPrefs.{given PreferredDrink, given PreferredPrompt}
    Greeter.greet("Tom")
  }
//  import com.peknight.demo.scala3.givens.TomsPrefs.given Ordering[PreferredPrompt]
//  import com.peknight.demo.scala3.givens.TomsPrefs.given Ordering[?]
  {
    Greeter.greet("Jill")(using JillsPrefs.prompt, JillsPrefs.drink)
  }

  def isort[T](xs: List[T])(using Ord[T]): List[T] =
    if xs.isEmpty then Nil
    else insert(xs.head, isort(xs.tail))

  def insert[T: Ord](x: T, xs: List[T]): List[T] =
    if xs.isEmpty || x <= xs.head then x :: xs
    else xs.head :: insert(x, xs.tail)

  println(isort(List(4, -10, 10)))
  println(isort(List("cherry", "blackberry", "apple", "pear")))
  println(isort(List(Rational(7, 8), Rational(5, 6), Rational(1, 2))))

