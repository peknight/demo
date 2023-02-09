package com.peknight.demo.scalatest.matcher

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class CheckingSizeAndLengthFunSuite extends AnyFunSuite:
  test("result should have length 3") {
    val result = List(1, 2, 3)
    result should have length 3
    result should have size 3
  }
