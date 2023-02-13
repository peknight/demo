package com.peknight.demo.scalatest.matcher

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class WorkingWithIteratorsFunSuite extends AnyFunSuite:
  test("conert your iterators to a sequence explicitly before using them in a matcher expressions") {
    val it = List(1, 2, 3).iterator
    it.to(LazyList) should contain (2)
  }
