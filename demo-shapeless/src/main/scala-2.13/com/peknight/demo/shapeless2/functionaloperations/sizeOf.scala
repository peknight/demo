package com.peknight.demo.shapeless2.functionaloperations

import shapeless._

object sizeOf extends Poly1 {
  implicit val intCase: Case.Aux[Int, Int] = at(identity)
  implicit val stringCase: Case.Aux[String, Int] = at(_.length)
  implicit val booleanCase: Case.Aux[Boolean, Int] = at(bool => if (bool) 1 else 0)
}
