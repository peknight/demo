package com.peknight.demo.scalacheck.quickexample

import org.scalacheck.Prop.forAll

object QuickExampleApp extends App:

  val propConcatLists = forAll { (l1: List[Int], l2: List[Int]) =>
    l1.size + l2.size == (l1 ::: l2).size
  }
  propConcatLists.check()

  val propSqrt = forAll { (n: Int) => scala.math.sqrt(n * n) == n }
  propSqrt.check()


