package com.peknight.demo.shapeless.functionaloperations

import shapeless._

object conversions extends Poly1 {
  implicit val intCase: Case.Aux[Int, Boolean] = at(_ > 0)
  implicit val boolCase: Case.Aux[Boolean, Int] = at(if (_) 1 else 0)
  implicit val strCase: Case.Aux[String, String] = at(identity)
}
