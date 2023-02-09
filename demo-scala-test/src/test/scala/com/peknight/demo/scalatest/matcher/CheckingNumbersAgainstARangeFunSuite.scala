package com.peknight.demo.scalatest.matcher

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class CheckingNumbersAgainstARangeFunSuite extends AnyFunSuite:
  test("whether a number is within a range") {
    val sevenDotOh = 7.0
    sevenDotOh should equal (6.9 +- 0.2)
    sevenDotOh should === (6.9 +- 0.2)
    sevenDotOh should be (6.9 +- 0.2)
    sevenDotOh shouldEqual 6.9 +- 0.2
    sevenDotOh shouldBe 6.9 +- 0.2
  }

  test("use +- with any type T for which an implicit Numeric[T] exists") {
    val seven = 7
    seven should equal (6 +- 2)
    seven should === (6 +- 2)
    seven should be (6 +- 2)
    seven shouldEqual 6 +- 2
    seven shouldBe 6 +- 2
  }