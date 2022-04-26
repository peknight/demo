package com.peknight.demo.spire.overview

import spire.syntax.literals.*

object LiteralsDemo extends App:
  // bytes without type annotation!
  val x = b"100"
  // shorts
  val y = h"999"
  // unsigned constant converted to signed (-1)
  val mask = b"255"
  println(s"x = $x, y = $y, mask = $mask")
  // rationals
  val n1 = r"1/3"
  // simplified at compile-time to 13/942
  val n2 = r"1599/115866"
  println(s"n1 = $n1, n2 = $n2")
