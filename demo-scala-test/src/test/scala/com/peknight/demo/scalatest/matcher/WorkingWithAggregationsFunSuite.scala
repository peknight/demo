package com.peknight.demo.scalatest.matcher

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class WorkingWithAggregationsFunSuite extends AnyFunSuite:
  test("at least one of the specified objects are contained in the containing object") {
    List(1, 2, 3) should contain atLeastOneOf (2, 3, 4)
    Array(1, 2, 3) should contain atLeastOneOf (3, 4, 5)
    "abc" should contain atLeastOneOf ('c', 'a', 't')
  }
