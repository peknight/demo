package com.peknight.demo.shapeless.countingtypes

import com.peknight.demo.shapeless.derivation.Tree
import com.peknight.demo.shapeless.introduction.IceCream
import org.scalacheck.*

import scala.Tuple.Size
import scala.compiletime.ops.int.S
import scala.compiletime.{constValue, erasedValue}
import scala.deriving.Mirror

object CountingWithTypesApp extends App:

  type Zero = Size[EmptyTuple]
  type One = S[Zero]
  type Two = S[One]

  val toInt = constValue[Two]
  println(toInt)
  println(constValue[Size[String *: Int *: Boolean *: EmptyTuple]])

  def sizeOf[A](using size: SizeOf[A]): Int = size.value
  println(sizeOf[IceCream])

  for i <- 1 to 3 do println(Arbitrary.arbitrary[Int].sample)
  for i <- 1 to 3 do println(Arbitrary.arbitrary[(Boolean, Byte)].sample)

  def random[A](using r: Random[A]): A = r.get
  for i <- 1 to 3 do println(random[Int])
  for i <- 1 to 3 do println(random[Char])

  for i <- 1 to 5 do println(random[Cell])
  for i <- 1 to 5 do println(random[Light])

  val tuple = 123 *: "foo" *: true *: 'x' *: EmptyTuple
  println(tuple(1))
  println(tuple(3))
  println(tuple.take(3).drop(1))
  // shapeless2的updatedAt方法暂未找到替代
