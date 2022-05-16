package com.peknight.demo.scala.enums

import com.peknight.demo.scala.enums.Direction.*
import com.peknight.demo.scala.enums.Eastwood.*
import com.peknight.demo.scala.enums.Literal.*
import com.peknight.demo.scala.enums.Seinfeld.*

object EnumsApp extends App:
  println(North.invert)
  println(East.invert)
  println(North.ordinal)
  println(East.ordinal)
  println(South.ordinal)
  println(West.ordinal)
  Direction.values.foreach(println)
  println(Direction.valueOf("North"))
  println(Direction.valueOf("East"))
  // println(Direction.valueOf("Up"))
  println(North.degrees)
  println(South.degrees)
  println(allButNeartest(42))
  val eastWood = Good(41)
  println(eastWood.map(_ + 1))
  val xs = 1 :: 2 :: 3 :: Nada
  println(xs)
  println(valueOfLiteral(BooleanLit(true)))
  println(valueOfLiteral(IntLit(42)))
