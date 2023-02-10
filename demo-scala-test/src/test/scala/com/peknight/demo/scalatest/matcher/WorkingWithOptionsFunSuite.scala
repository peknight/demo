package com.peknight.demo.scalatest.matcher

import org.scalatest.OptionValues.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class WorkingWithOptionsFunSuite extends AnyFunSuite:
  test("check whether an option is None") {
    val option = Option.empty[Int]
    option shouldEqual None
    option shouldBe None
    option should === (None)
    option shouldBe empty
  }

  test("check whether an option is defined") {
    val option = Option("hi")
    option shouldEqual Some("hi")
    option shouldBe Some("hi")
    option should === (Some("hi"))
    option shouldBe defined
  }

  test("option should be defined and then say something else abount its value") {
    val option = Option(1)
    option.value should be < (7)
  }

  test("use contain, contain oneOf, contain noneOf syntax with options") {
    Some(2) should contain (2)
    Some(7) should contain oneOf(5, 7, 9)
    Some(0) should contain noneOf(7, 8, 9)
  }
