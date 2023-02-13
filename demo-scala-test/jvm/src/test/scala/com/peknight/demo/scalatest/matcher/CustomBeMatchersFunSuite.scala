package com.peknight.demo.scalatest.matcher

import com.peknight.demo.scalatest.matcher.OddEvenMatchers.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class CustomBeMatchersFunSuite extends AnyFunSuite:
  test("num shoud be odd") {
    val num = 7
    num shouldBe odd
    num should not be even
  }
