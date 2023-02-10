package com.peknight.demo.scalatest.matcher

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class CheckingCodeCompileFunSuite extends AnyFunSuite:
  test("checking that a snippet of code does not compile") {
    "val a: String = 1" shouldNot compile
    "val a: String = 1" shouldNot typeCheck
    "val a: Int = 1" should compile
  }
