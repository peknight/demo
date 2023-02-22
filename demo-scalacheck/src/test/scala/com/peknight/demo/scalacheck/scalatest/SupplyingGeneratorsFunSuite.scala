package com.peknight.demo.scalacheck.scalatest

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalacheck.Gen

class SupplyingGeneratorsFunSuite extends AnyFunSuite with ScalaCheckPropertyChecks:
  val evenInts = for n <- Gen.choose(-1000, 1000) yield 2 * n
  test("use evenInts on a property check like this") {
    forAll (evenInts) { n => n % 2 should equal (0) }
  }

  val validNumers = for n <- Gen.choose(Integer.MIN_VALUE + 1, Integer.MAX_VALUE) yield n
  val validDenoms = for d <- validNumers if d != 0 yield d
  test("use validNumers and validDenoms in the property check like this") {
    forAll (validNumers, validDenoms) { (n: Int, d: Int) =>
      whenever (d != 0 && d != Integer.MIN_VALUE && n != Integer.MIN_VALUE) {
        val f = new Fraction(n, d)
        if n < 0 && d < 0 || n > 0 && d > 0 then f.numer should be > 0
        else if n != 0 then f.numer should be < 0
        else f.numer shouldBe 0
        f.denom should be > 0
      }
    }
  }

  test("supplying both generators and argument names") {
    forAll((validNumers, "n"), (validDenoms, "d")) { (n: Int, d: Int) =>
      whenever(d != 0 && d != Integer.MIN_VALUE && n != Integer.MIN_VALUE) {
        val f = new Fraction(n, d)
        if n < 0 && d < 0 || n > 0 && d > 0 then f.numer should be > 0
        else if n != 0 then f.numer should be < 0
        else f.numer shouldBe 0
        f.denom should be > 0
      }
    }
  }
