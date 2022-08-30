package com.peknight.demo.shapeless.hlistcoproduct

import com.peknight.demo.shapeless.hlistcoproduct.Penultimate.PenultimateOps
import com.peknight.demo.shapeless.introduction.IceCream
import shapeless._

object HListCoproductApp extends App {
  println(("Hello" :: 123 :: true :: HNil).last)
  println(("Hello" :: 123 :: true :: HNil).init)
  // HNil.last

  type BigList = String :: Int :: Boolean :: Double :: HNil
  val bigList: BigList = "foo" :: 123 :: true :: 456.0 :: HNil
  println(Penultimate[BigList].apply(bigList))

  type TinyList = String :: HNil
  val tinyList = "bar" :: HNil
  // Penultimate[TinyList].apply(tinyList)

  println(bigList.penultimate)

  println(IceCream("Sundae", 1, false).penultimate)
}
