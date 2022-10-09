package com.peknight.demo.shapeless2.functionaloperations

import shapeless._

object sum extends Poly2 {
  implicit val intIntCase: Case.Aux[Int, Int, Int] = at((a, b) => a + b)
  implicit val intStringCase: Case.Aux[Int, String, Int] = at((a, b) => a + b.length)
}
