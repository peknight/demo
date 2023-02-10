package com.peknight.demo.scalatest.matcher

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class CreatingDynamicMatchersFunSuite extends AnyFunSuite:
  test("Creating dynamic matchers") {
    val hidden = Symbol("hidden")
    // new java.io.File("secret.txt") should be (hidden)
    // new java.io.File("secret.txt") shouldBe hidden
  }
