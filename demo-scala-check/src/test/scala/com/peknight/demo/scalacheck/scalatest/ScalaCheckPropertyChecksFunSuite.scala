package com.peknight.demo.scalacheck.scalatest

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class ScalaCheckPropertyChecksFunSuite extends AnyFunSuite with ScalaCheckPropertyChecks:
  test("the passed values for numerator and denominator are properly normalized") {
    forAll { (n: Int, d: Int) =>
      whenever (d != 0 && d != Integer.MIN_VALUE && n != Integer.MIN_VALUE) {
        val f = new Fraction(n, d)
        if n < 0 && d < 0 || n > 0 && d > 0 then f.numer should be > 0
        else if n != 0 then f.numer should be < 0
        else f.numer shouldBe 0

        f.denom should be > 0
      }
    }
  }

  val invalidCombos =
    Table(
      ("n", "d"),
      (Integer.MIN_VALUE, Integer.MIN_VALUE),
      (1, Integer.MIN_VALUE),
      (Integer.MIN_VALUE, 1),
      (Integer.MIN_VALUE, 0),
      (1, 0)
    )

  test("use a table-driven property check to test that all combinations of invalid values passed to the" +
    "Fraction constructor produce the expected IllegalArgumentException") {
    forAll (invalidCombos) { (n: Int, d: Int) =>
      // evaluating { new Fraction(n, d) } should produce [IllegalArgumentException]
    }
  }
