package com.peknight.demo.scalatest.matcher

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class StringsAndArraysAsCollectionsFunSuite extends AnyFunSuite:
  test("You can use all the syntax for Scala and Java collections on ArrayS and StringS") {
    atLeast (2, Array(1, 2, 3)) should be > 1
    atMost (2, "halloo") shouldBe 'o'
    Array(1, 2, 3) shouldBe sorted
    "abcdefg" shouldBe sorted
    Array(1, 2, 3) should contain atMostOneOf (3, 4, 5)
    "abc" should contain atMostOneOf ('c', 'd', 'e')
  }
