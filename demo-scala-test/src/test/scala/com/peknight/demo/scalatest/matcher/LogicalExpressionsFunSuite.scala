package com.peknight.demo.scalatest.matcher

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class LogicalExpressionsFunSuite extends AnyFunSuite:
  test("combine matcher expressions with and and/or or") {
    val map = Map("two" -> 1)
    map should (contain key("two") and not contain value (7))

    val number = 8
    number should (be > (0) and be <= (10))
    val option = Option.empty[List[Int]]
    option should (equal (Some(List(1, 2, 3))) or be (None))
    val string = "fee"
    string should (equal ("fee") or equal ("fie") or equal ("foe") or equal ("fum"))
  }

  test("expressions with `and` and `or` do not short-circuit") {
    // "yellow" should (equal ("blue") and equal { println("hello, world!"); "green" })
    "yellow" should (equal ("blue") or equal { println("hello, world!"); "yellow" })
  }

  test("if you attempt to and a null check on a variable with an expression that uses the variable") {
    // val map: Map[String, Int] = null
    val map: Map[String, Int] = Map("ouch" -> 1)
    // map should (not be (null) and contain key ("ouch"))
    map should not be (null)
    map should contain key ("ouch")
  }

  test("`and` and `or` have the same precedence (difference with Boolean operators: && has a higher precedence than ||)") {
    val traversable = List(7)
    traversable should (contain (7) or (contain (8) and have size (9)))
  }



