package com.peknight.demo.scala3.assertionstests

import com.peknight.demo.scala3.compositioninheritance.Element.elem
import org.scalatest.funsuite.AnyFunSuite

class ElementSuite extends AnyFunSuite:

  val ele = elem('x', 2, 3)

  test("elem result should have passed width") {
    assert(ele.width == 2)
  }

  assertResult(2) {
    ele.width
  }

  // assertThrows[IllegalArgumentException] {
  //   elem('x', -2, 3)
  // }

  val caught = intercept[ArithmeticException] { 1 / 0 }
  assert(caught.getMessage == "/ by zero")

