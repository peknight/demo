package com.peknight.demo.scalatest.matcher

import org.scalactic.StringNormalizations.lowerCased
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class WorkingWithContainersFunSuite extends AnyFunSuite:
  test("whether a collection contains a particular element") {
    val traversable = List("one", "two", "three", "four", "five")
    traversable should contain ("five")
    List(1, 2, 3) should contain (2)
    Map('a' -> 1, 'b' -> 2, 'c' -> 3) should contain ('b' -> 2)
    Set(1, 2, 3) should contain (2)
    Array(1, 2, 3) should contain (2)
    "123" should contain ('2')
    Some(2) should contain (2)

    (List("Hi", "Di", "Ho") should contain ("ho")) (after being lowerCased)
  }

  test("whether an object contains another object") {
    List(1, 2, 3, 4, 5) should contain oneOf (5, 7, 9)
    Some(7) should contain oneOf (5, 7, 9)
    "howdy" should contain oneOf ('a', 'b', 'c', 'd')

    (Array("Doe", "Ray", "Me") should contain oneOf("X", "RAY", "BEAM")) (after being lowerCased)
  }

  test("none of the specified elements are contained") {
    List(1, 2, 3, 4, 5) should contain noneOf (7, 8, 9)
    Some(0) should contain noneOf (7, 8, 9)
    "12345" should contain noneOf ('7', '8', '9')
  }
