package com.peknight.demo.scalatest.option

import org.scalatest.OptionValues.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class OptionValuesFunSuite extends AnyFunSuite:
  test("option value should greater than 9") {
    val opt: Option[Int] = Option(16)
    opt.value should be > 9
    assert(opt.value > 9)
  }

  test("it would throw a NoSuchElementException") {
    // val opt: Option[Int] = None
    val opt: Option[Int] = Option(16)
    opt.get should be > 9
  }

  test("NoSuchElementException would cause the test to fail") {
    // val opt: Option[Int] = None
    val opt: Option[Int] = Option(16)
    opt should be(Symbol("defined"))
    opt.get should be > 9
  }



