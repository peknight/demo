package com.peknight.demo.shapeless2.adtandgeneric

import com.peknight.demo.shapeless2.introduction.{Employee, IceCream}
import shapeless.{::, Generic, HNil}

object GenericProductEncodingsApp extends App {
  val product: String :: Int :: Boolean :: HNil = "Sundae" :: 1 :: false :: HNil
  val first = product.head
  println(first)
  val second = product.tail.head
  println(second)
  val rest = product.tail.tail
  println(rest)
  // product.tail.tail.tail.head // 报错

  val newProduct = 42L :: product

  val iceCreamGen = Generic[IceCream]

  val iceCream = IceCream("Sundae", 1, false)

  val repr = iceCreamGen.to(iceCream)
  println(repr)

  val iceCream2 = iceCreamGen.from(repr)
  println(iceCream2)

  val employee = Generic[Employee].from(Generic[IceCream].to(iceCream))
  println(employee)

  val tupleGen = Generic[(String, Int, Boolean)]

  println(tupleGen.to(("Hello", 123, true)))
  println(tupleGen.from("Hello" :: 123 :: true :: HNil))

  println(Generic[BigData].from(Generic[BigData].to(BigData(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17,
    18, 19, 20, 21, 22, 23))))

}
