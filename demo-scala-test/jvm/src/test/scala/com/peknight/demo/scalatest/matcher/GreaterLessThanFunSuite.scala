package com.peknight.demo.scalatest.matcher

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class GreaterLessThanFunSuite extends AnyFunSuite:
  test("greater than, less than, greater than or equal, or less than or equal") {
    val one = 1
    one should be < 7
    one should be > 0
    one should be <= 7
    one should be >= 0
  }
