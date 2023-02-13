package com.peknight.demo.scalatest.matcher

import org.scalatest.Inspectors.forAll
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class InspectorShorthandsFunSuite extends AnyFunSuite:
  test("You can use the Inspectors syntax with matchers as well as assertions") {
    val xs = List(1, 2, 3)
    val yss = List(xs, xs, xs)
    forAll(yss) { ys => forAll (ys) { y => y should be > 0} }

    // forAll (xs) { x => x should be < 10 }
    all (xs) should be < 10
  }

  test("All of the inspectors have shorthands in matchers") {
    val xs = List(1, 2, 3, 4, 5)
    all (xs) should be > 0
    atMost (2, xs) should be >= 4
    atLeast (3, xs) should be < 5
    between (2, 3, xs) should (be > 1 and be < 5)
    exactly (2, xs) should be <= 2
    every (xs) should be < 10
    // exactly (2, xs) shouldEqual 2
  }

