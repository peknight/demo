package com.peknight.demo.js.lihaoyi.handson.workbench.example

import scala.annotation.tailrec

case class RNG(seed: Long) {
  def nextInt: (Int, RNG) = {
    val newSeed = (seed * 0x5DEECE66DL + 0XBL) & 0xFFFFFFFFFFFFL
    val nextRNG = RNG(newSeed)
    val n = (newSeed >>> 16).toInt
    (n, nextRNG)
  }

  def nonNegativeInt: (Int, RNG) = {
    val (value, rng) = nextInt
    (if (value >= 0) value else -(value + 1), rng)
  }

  def nonNegativeLessThan(n: Int): (Int, RNG) = {
    RNG.nonNegativeLessThan(this, n)
  }
}

object RNG {
  @tailrec
  def nonNegativeLessThan(rng: RNG, n: Int): (Int, RNG) = {
    val (i, rng2) = rng.nonNegativeInt
    val mod = i % n
    if (i + (n - 1) - mod >= 0) (mod, rng2)
    else nonNegativeLessThan(rng2, n)
  }
}
