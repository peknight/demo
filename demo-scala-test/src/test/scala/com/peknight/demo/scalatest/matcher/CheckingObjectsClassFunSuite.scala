package com.peknight.demo.scalatest.matcher

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class CheckingObjectsClassFunSuite extends AnyFunSuite:
  class Tiger()
  trait Orangutan
  trait Fruit

  test("an object is an instance of a particular class or trait") {
    val result1 = new Tiger()
    result1 shouldBe a [Tiger]
    result1 should not be an [Orangutan]
  }

  test("insert an underscore for any type parameters") {
    val result = List.empty[Fruit]
    // recommended
    result shouldBe a [List[_]]
    // discouraged
    result shouldBe a [List[Fruit]]
  }

