package com.peknight.demo.spire.overview

import spire.syntax.literals.si.*

object SIDemo extends App:
  // Int
  val w = i"1 944 234 123"
  // Long
  val x = j"89 234 614 123 234 772"
  // BigInt
  val y = big"123 234 435 456 567 678 234 123 112 234 345"
  // BigDecimal
  val z = dec"1 234 456 789.123456789098765"
  println(s"w = $w, x = $x, y = $y, z = $z")
