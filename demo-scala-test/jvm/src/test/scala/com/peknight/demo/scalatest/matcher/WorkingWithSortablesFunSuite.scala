package com.peknight.demo.scalatest.matcher

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class WorkingWithSortablesFunSuite extends AnyFunSuite:
  test("whether the elemetns of \"sortable\" objects are in sorted order") {
    List(1, 2, 3) shouldBe sorted
  }
