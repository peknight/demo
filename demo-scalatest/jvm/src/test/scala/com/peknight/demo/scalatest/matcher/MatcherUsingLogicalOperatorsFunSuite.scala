package com.peknight.demo.scalatest.matcher

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.{MatchResult, Matcher}
import org.scalatest.matchers.should.Matchers.*

class MatcherUsingLogicalOperatorsFunSuite extends AnyFunSuite:
  val beWithinTolerance = be >= 0 and be <= 10

  test("number is within the tolerance") {
    val num = 1
    num should beWithinTolerance
  }

  // val beOdd = new Matcher[Int]:
  //   def apply(left: Int) = MatchResult(
  //     left % 2 == 1,
  //     s"$left was not odd",
  //     s"$left was odd"
  //   )

  val beOdd = Matcher { (left: Int) => MatchResult(
    left % 2 == 1,
    s"$left was not odd",
    s"$left was odd"
  )}

  test("use beOdd like this") {
    3 should beOdd
    4 should not (beOdd)
  }