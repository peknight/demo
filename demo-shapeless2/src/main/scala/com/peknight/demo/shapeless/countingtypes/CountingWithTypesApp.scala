package com.peknight.demo.shapeless.countingtypes

import com.peknight.demo.shapeless.introduction.IceCream
import org.scalacheck._
import shapeless._
import shapeless.ops.coproduct
import shapeless.ops.hlist.Length
import shapeless.ops.nat.ToInt

object CountingWithTypesApp extends App {
  type Zero = Nat._0
  type One = Succ[Zero]
  type Two = Succ[One]
  // Nat._1
  // Nat._2
  // Nat._3
  val toInt = ToInt[Two]
  println(toInt.apply())
  println(Nat.toInt[Nat._3])
  val hlistLength = Length[String :: Int :: Boolean :: HNil]
  val coproductLength = coproduct.Length[Double :+: Char :+: CNil]

  println(Nat.toInt[hlistLength.Out])
  println(Nat.toInt[coproductLength.Out])

  def sizeOf[A](implicit size: SizeOf[A]): Int = size.value
  println(sizeOf[IceCream])

  for (i <- 1 to 3) println(Arbitrary.arbitrary[Int].sample)
  for (i <- 1 to 3) println(Arbitrary.arbitrary[(Boolean, Byte)].sample)

  def random[A](implicit r: Random[A]): A = r.get
  for (i <- 1 to 3) println(random[Int])
  for (i <- 1 to 3) println(random[Char])

  for (i <- 1 to 5) println(random[Cell])
  for (i <- 1 to 5) println(random[Light])

  val hlist = 123 :: "foo" :: true :: 'x' :: HNil
  println(hlist.apply[Nat._1])
  println(hlist.apply[Nat._3])
  println(hlist.take(Nat._3).drop(Nat._1))
  println(hlist.updatedAt(Nat._1, "bar").updatedAt(Nat._2, "baz"))
}
