package com.peknight.demo.scala.functionalobjects

object FunctionalObjectsApp extends App {
  val oneHalf = Rational(1, 2)
  println(oneHalf)
  val twoThirds = Rational(2, 3)
  println(oneHalf + twoThirds)
  Rational(3)
  Rational(66, 42)
  println(oneHalf + oneHalf * twoThirds)
  println((oneHalf + oneHalf) * twoThirds)
  println(oneHalf + (oneHalf * twoThirds))
  println(2 * twoThirds)
}
