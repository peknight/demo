package com.peknight.demo.shapeless.functionaloperations

object myPoly extends Poly:
  given Case[this.type, Int] with
    type Result = Double
    def apply(num: Int): Double = num / 2.0

  given Case[this.type, String] with
    type Result = Int
    def apply(str: String): Int = str.length
