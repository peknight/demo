package com.peknight.demo.scalatest.matcher

import org.scalatest.Inside.inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class CheckingExpressionMatchesPatternFunSuite extends AnyFunSuite:

  val name = Name("Jane", "Q", "Programmer")

  test("Inside trait allows you to make assertions after a pattern match") {
    inside(name) { case Name(first, _, _) =>
      // first should startWith("S")
      first should startWith("J")
    }
  }

  test("a better alternative is matchPattern") {
    // name should matchPattern { case Name("Sarah", _, _) => }
    name should matchPattern { case Name("Jane", _, _) => }
  }
