package com.peknight.demo.shapeless2.adtandgeneric

import com.peknight.demo.shapeless2.adtandgeneric.Shape._

object AlgebraicDataTypesApp extends App {
  val rect: Shape = Rectangle(3.0, 4.0)
  val circ: Shape = Circle(1.0)
  println(area(rect))
  println(area(circ))

  val rect2: Shape2 = Left((3.0, 4.0))
  val circ2: Shape2 = Right(1.0)
}
