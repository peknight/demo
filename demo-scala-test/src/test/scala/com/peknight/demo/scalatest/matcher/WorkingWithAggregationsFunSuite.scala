package com.peknight.demo.scalatest.matcher

import org.scalactic.StringNormalizations.{lowerCased, trimmed}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class WorkingWithAggregationsFunSuite extends AnyFunSuite:
  test("at least one of the specified objects are contained in the containing object") {
    List(1, 2, 3) should contain atLeastOneOf (2, 3, 4)
    Array(1, 2, 3) should contain atLeastOneOf (3, 4, 5)
    "abc" should contain atLeastOneOf ('c', 'a', 't')

    (Vector(" A", "B ") should contain atLeastOneOf ("a ", "b", "c")) (after being lowerCased and trimmed)
  }

  test("at most one of which should be contained in the containing object") {
    List(1, 2, 3, 4, 5) should contain atMostOneOf (5, 6, 7)
  }

  test("should all be contained in the containing object") {
    List(1, 2, 3, 4, 5) should contain allOf (2, 3, 5)
  }

  test("the containing object contains only the specified objects, though it may contain more than one of each") {
    List(1, 2, 3, 2, 1) should contain only (1, 2, 3)
  }

  test("two aggregations contain the same objects") {
    List(1, 2, 2, 3, 3, 3) should contain theSameElementsAs Vector(3, 2, 3, 1, 2, 3)
  }
