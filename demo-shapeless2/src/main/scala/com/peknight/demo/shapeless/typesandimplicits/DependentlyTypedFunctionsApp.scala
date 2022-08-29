package com.peknight.demo.shapeless.typesandimplicits

import shapeless.ops.hlist.Last
import shapeless.{::, HNil, the}

object DependentlyTypedFunctionsApp extends App {
  val last1 = Last[String :: Int :: HNil]
  val last2 = Last[Int :: String :: HNil]

  println(last1("foo" :: 123 :: HNil))
  println(last2(321 :: "bar" :: HNil))

  println(the[Last[String :: Int :: HNil]])

  val second1 = Second[String :: Boolean :: Int :: HNil]
  val second2 = Second[String :: Int :: Boolean :: HNil]
  // Second[String :: HNil]

  println(second1("foo" :: true :: 123 :: HNil))
  println(second2("bar" :: 321 :: false :: HNil))
  // second1("baz" :: HNil)
}
