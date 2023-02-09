package com.peknight.demo.scalatest.matcher

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class CheckingObjectIdentityFunSuite extends AnyFunSuite:
  test("two references refer to the exact same object") {
    val ref1 = new Object()
    val ref2 = ref1
    ref1 should be theSameInstanceAs ref2
  }
