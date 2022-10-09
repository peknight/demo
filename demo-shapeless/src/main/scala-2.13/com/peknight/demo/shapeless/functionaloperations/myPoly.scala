package com.peknight.demo.shapeless.functionaloperations

import shapeless._

object myPoly extends Poly1 {

  implicit def intCase: Case.Aux[Int, Double] = at(num => num / 2.0)

  implicit def stringCase: Case.Aux[String, Int] = at(str => str.length)
}
