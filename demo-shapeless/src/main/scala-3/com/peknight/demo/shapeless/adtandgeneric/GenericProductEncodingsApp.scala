package com.peknight.demo.shapeless.adtandgeneric

import com.peknight.demo.shapeless.introduction.{Employee, IceCream}

import scala.deriving.Mirror

object GenericProductEncodingsApp extends App:
  val product: String *: Int *: Boolean *: EmptyTuple = "Sundae" *: 1 *: false *: EmptyTuple
  val first = product.head
  println(first)
  val second = product.tail.head
  println(second)
  val rest = product.tail.tail
  println(rest)
  // product.tail.tail.tail.head // 报错

  val newProduct = 42L *: product
  println(newProduct)

  val iceCreamGen = summon[Mirror.Of[IceCream]]

  val iceCream = IceCream("Sundae", 1, false)

  val repr = Tuple.fromProductTyped(iceCream)
  println(repr)

  val iceCream2 = iceCreamGen.fromProduct(repr)
  println(iceCream2)

  val employee = summon[Mirror.Of[Employee]].fromProduct(Tuple.fromProductTyped(iceCream))
  println(employee)

  // 在Scala3中毫无意义
  val tupleGen = summon[Mirror.Of[(String, Int, Boolean)]]
  println(Tuple.fromProductTyped(("Hello", 123, true)))
  println(tupleGen.fromProduct("Hello" *: 123 *: true *: EmptyTuple))

  println(summon[Mirror.Of[BigData]].fromProduct(Tuple.fromProductTyped(BigData(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
    13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23))))