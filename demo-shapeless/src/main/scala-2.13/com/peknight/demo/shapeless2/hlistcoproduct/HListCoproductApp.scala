package com.peknight.demo.shapeless2.hlistcoproduct

import com.peknight.demo.shapeless2.hlistcoproduct.Migration._
import com.peknight.demo.shapeless2.hlistcoproduct.Penultimate.PenultimateOps
import com.peknight.demo.shapeless2.introduction.IceCream
import shapeless._
import shapeless.record._

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

  // 啊 这实在是太美妙了
  println(IceCreamV1("Sundae", 1, true).migrateTo[IceCreamV2a])
  println(IceCreamV1("Sundae", 1, true).migrateTo[IceCreamV2b])
  // 就是idea的编译器不太美妙
  println(IceCreamV1("Sundae", 1, true).migrateTo[IceCreamV2c])

  val sundae = LabelledGeneric[IceCream].to(IceCream("Sundae", 1, false))
  println(sundae)

  println(sundae.get(Symbol("name")))
  println(sundae.get(Symbol("numCherries")))
  // println(sundae.get(Symbol("nomCherries")))

  println(sundae.updated(Symbol("numCherries"), 3))
  println(sundae.remove(Symbol("inCone")))

  println(sundae.updateWith(Symbol("name"))(name => s"MASSIVE $name"))
  println(sundae.toMap)
}
