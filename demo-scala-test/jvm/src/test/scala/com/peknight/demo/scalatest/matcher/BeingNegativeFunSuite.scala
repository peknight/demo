package com.peknight.demo.scalatest.matcher

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class BeingNegativeFunSuite extends AnyFunSuite:
  test("check the opposite of some condition") {
    val result = "abc"
    result should not be (null)
    val sum = 16
    sum should not be <= (10)
    val myList = List(1)
    val yourList = List(2)
    myList should not equal (yourList)
    val string = "Happy new year!"
    string should not startWith ("Hello")
  }
