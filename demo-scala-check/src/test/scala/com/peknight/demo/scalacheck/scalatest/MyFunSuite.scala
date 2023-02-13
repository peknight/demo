package com.peknight.demo.scalacheck.scalatest

import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.Checkers
import org.scalacheck.Arbitrary.*
import org.scalacheck.Prop.*
import org.scalacheck.Test.Parameters


class MyFunSuite extends AnyFunSuite with Checkers:
  test("testConcat") {
    check((a: List[Int], b: List[Int]) => a.size + b.size == (a ::: b).size)
  }

  test("minSuccessful") {
    check((n: Int) => n + 0 == n, minSuccessful(500))
  }

  test("minSuccessful and maxDiscarded") {
    check((n: Int) => n + 0 == n, minSuccessful(500), maxDiscardedFactor(300))
  }