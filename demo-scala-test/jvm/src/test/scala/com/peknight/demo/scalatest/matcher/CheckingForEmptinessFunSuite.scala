package com.peknight.demo.scalatest.matcher

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.enablers.Emptiness.emptinessOfAnyRefWithIsEmptyMethod

class CheckingForEmptinessFunSuite extends AnyFunSuite:
  test("whether an object is empty") {
    val traversable = List.empty[Int]
    val javaMap = new java.util.HashMap[String, Int]()
    javaMap.put("a", 1)
    traversable shouldBe empty
    javaMap should not be empty
    None shouldBe empty
    Some(1) should not be empty
    "" shouldBe empty
    new java.util.HashMap[Int, Int] shouldBe empty
    val obj: AnyRef { def isEmpty: Boolean } = new { def isEmpty = true }
    obj shouldBe empty
    Array(1, 2, 3) should not be empty
  }
