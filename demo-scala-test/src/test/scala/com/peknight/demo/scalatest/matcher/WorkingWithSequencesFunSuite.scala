package com.peknight.demo.scalatest.matcher

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class WorkingWithSequencesFunSuite extends AnyFunSuite:
  test("the containing object contains only the specified objects, in order") {
    List(1, 2, 2, 3, 3, 3) should contain inOrderOnly (1, 2, 3)
  }
  test("the containing object contains only the specified objects in order, but allows other objects appear" +
    "in the left-hand aggregation as well") {
    List(0, 1, 2, 2, 99, 3, 3, 3, 5) should contain inOrder (1, 2, 3)
  }
  test("two aggregations contain the same exact elements in the same (iteration) order") {
    List(1, 2, 3) should contain theSameElementsInOrderAs collection.mutable.TreeSet(3, 2, 1)
  }
