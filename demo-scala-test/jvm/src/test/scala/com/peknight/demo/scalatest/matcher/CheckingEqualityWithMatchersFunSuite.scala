package com.peknight.demo.scalatest.matcher

import org.scalactic.StringNormalizations.lowerCased
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class CheckingEqualityWithMatchersFunSuite extends AnyFunSuite:

  test("five different ways to check equality") {
    val result = 3
    // can customize equality
    result should equal (3)
    // can customize equality and enforce type constraints
    result should === (3)
    // cannot customize equality, so fastest to compile
    result should be (3)
    // can customize equality, no parentheses required
    result shouldEqual 3
    // cannot customize equality, so fastest to compile, no parentheses required
    result shouldBe 3
  }

  given[T]: CanEqual[Array[T], Array[T]] = CanEqual.derived
  test("array equality") {
    // yields false
    // assert(Array(1, 2) == Array(1, 2))
    // succeeds
    Array(1, 2) should equal (Array(1, 2))
  }

  test("supply implicit parameters explicitly") {
    "Hi" should equal ("hi") (after being lowerCased)
  }
