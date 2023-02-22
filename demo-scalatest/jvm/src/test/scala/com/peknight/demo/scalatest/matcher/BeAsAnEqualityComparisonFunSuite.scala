package com.peknight.demo.scalatest.matcher

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class BeAsAnEqualityComparisonFunSuite extends AnyFunSuite:
  test("This redundancy between be an equals exists in part because it enables syntax that sometimes sounds" +
    "more natural") {
    val result = null
    // result should equal (null)
    result should be (null)
  }

  test("some other examples of be used for equality comparison") {
    val sum = 7.0
    sum should be (7.0)
    val boring = false
    boring should be (false)
    val fun = true
    fun should be (true)
    val list = List.empty[Int]
    list should be (Nil)
    val option1 = Option.empty[Int]
    option1 should be (None)
    val option2 = Option(1)
    option2 should be (Some(1))

    Array(1, 2) should be (Array(1, 2))
  }
