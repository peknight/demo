package com.peknight.demo.shapeless.functionaloperations

import shapeless._

object valueAndSizeOf extends Poly1 {
  implicit val intCase: Case.Aux[Int, Int :: Int :: HNil] = at(num => num :: num :: HNil)
  implicit val stringCase: Case.Aux[String, String :: Int :: HNil] = at(str => str :: str.length :: HNil)
  implicit val booleanCase: Case.Aux[Boolean, Boolean :: Int :: HNil] = at(bool => bool :: (if (bool) 1 else 0) :: HNil)
}
