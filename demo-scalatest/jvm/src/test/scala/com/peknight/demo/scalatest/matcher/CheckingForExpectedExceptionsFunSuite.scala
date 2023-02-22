package com.peknight.demo.scalatest.matcher

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class CheckingForExpectedExceptionsFunSuite extends AnyFunSuite:
  test("check for an expected exception") {
    val s = "Hello, world!"
    an [IndexOutOfBoundsException] should be thrownBy s.charAt(-1)

    val thrown = the [IndexOutOfBoundsException] thrownBy s.charAt(-1)
    thrown.getMessage should equal ("String index out of range: -1")

    the [ArithmeticException] thrownBy 1 / 0 should have message "/ by zero"
    the [IndexOutOfBoundsException] thrownBy (s.charAt(-1)) should have message "String index out of range: -1"

    noException should be thrownBy 0 / 1
  }
