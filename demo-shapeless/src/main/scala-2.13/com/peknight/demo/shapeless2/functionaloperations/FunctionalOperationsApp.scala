package com.peknight.demo.shapeless2.functionaloperations

import com.peknight.demo.shapeless2.functionaloperations.ProductMapper._
import shapeless.HNil

object FunctionalOperationsApp extends App {
  println(myPoly.apply(123))
  println(myPoly.apply("hello"))
  println(multiply(3, 4))
  println(multiply(3, "4"))
  println(total(10))
  println(total(Option(20.0)))
  println(total(List(1L, 2L, 3L)))
  println(total(Option.empty[Int]))
  val a = myPoly.apply(123)
  val b: Double = a
  // val c: Double = myPoly.apply(123)
  val d: Double = myPoly.apply[Int](123)
  println((10 :: "hello" :: true :: HNil).map(sizeOf))
  // (1.5 :: HNil).map(sizeOf)
  println((10 :: "hello" :: true :: HNil).flatMap(valueAndSizeOf))
  // (10 :: "hello" :: true :: HNil).flatMap(sizeOf)
  println((10 :: "hello" :: 100 :: HNil).foldLeft(0)(sum))

  println(IceCream1("Sundae", 1, false).mapTo[IceCream2](conversions))
}
