package com.peknight.demo.shapeless.hlistcoproduct

import com.peknight.demo.shapeless.generic.mapper.identity.Mapper.{*, given}
import com.peknight.demo.shapeless.hlistcoproduct.Penultimate.*
import com.peknight.demo.shapeless.introduction.IceCream

object HListCoproductApp extends App:
  // println(("Hello" *: 123 *: true *: EmptyTuple).last)
  // println(("Hello" *: 123 *: true *: EmptyTuple).init)
  // EmptyTuple.last
  type BigList = String *: Int *: Boolean *: Double *: EmptyTuple
  val bigList: BigList = "foo" *: 123 *: true *: 456.0 *: EmptyTuple
  println(Penultimate[BigList].apply(bigList))

  type TinyList = String *: EmptyTuple
  val tinyList = "bar" *: EmptyTuple
  // Penultimate[TinyList].apply(tinyList)

  println(bigList.penultimate)

  println(IceCream("Sundae", 1, false).penultimate)

  // OHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH 2022-09-11 22:56 成功迁移Scala3
  println(IceCreamV1("Sundae", 1, true).to[IceCreamV2a])
  println(IceCreamV1("Sundae", 1, true).to[IceCreamV2b])
  println(IceCreamV1("Sundae", 1, true).to[IceCreamV2c])
