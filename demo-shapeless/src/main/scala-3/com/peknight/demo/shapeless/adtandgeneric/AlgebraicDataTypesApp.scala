package com.peknight.demo.shapeless.adtandgeneric

import com.peknight.demo.shapeless.adtandgeneric.Shape.*

object AlgebraicDataTypesApp extends App:
  val rect: Shape = Rectangle(3.0, 4.0)
  val circ: Shape = Circle(1.0)
  println(area(rect))
  println(area(circ))

  val rect2: Shape2 = (3.0, 4.0)
  val circ2: Shape2 = 1.0
  println(area2(rect2))
  println(area2(circ2))
