package com.peknight.demo.scalacheck.scalatest

class Fraction(n: Int, d: Int):
  require(d != 0)
  require(d != Integer.MIN_VALUE)
  require(n != Integer.MIN_VALUE)
  val numer = if d < 0 then -1 * n else n
  val denom = d.abs
  override def toString = s"$numer / $denom"
