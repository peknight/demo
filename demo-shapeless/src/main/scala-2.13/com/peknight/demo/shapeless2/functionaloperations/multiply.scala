package com.peknight.demo.shapeless2.functionaloperations

import shapeless._

object multiply extends Poly2 {
  implicit val intIntCase: Case.Aux[Int, Int, Int] = at((a, b) => a * b)
  implicit val intStrCase: Case.Aux[Int, String, String] = at((a, b) => b * a)
}
