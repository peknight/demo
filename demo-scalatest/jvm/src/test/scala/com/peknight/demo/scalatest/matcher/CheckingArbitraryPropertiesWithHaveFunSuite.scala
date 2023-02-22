package com.peknight.demo.scalatest.matcher

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class CheckingArbitraryPropertiesWithHaveFunSuite extends AnyFunSuite with BookPropertyMatchers:

  val book = Book("Programming in Scala", List("Odersky", "Spoon", "Venners"), 2008)

  test("check properties of any type") {
    book should have (
      Symbol("title") ("Programming in Scala"),
      Symbol("author") (List("Odersky", "Spoon", "Venners")),
      Symbol("pubYear") (2008)
    )
  }

  test("checking size and length does not require parentheses") {
    val array = Array(1, 2, 3)
    val set = Set.tabulate(90)(identity)
    array should have length (3)
    set should have size (90)
    // you can alternatively, write:
    array should have (length (3))
    set should have (size (90))
  }

  test("assert a book whose title is Moby Dick") {
    val book = Book("Moby Dick", List("Herman Melville"), 1851)
    // book should have (Symbol("title") ("A Tale of Two Cities"))
    book should have (Symbol("title") ("Moby Dick"))
  }

  test("check properties in a type-safe manner, use a HavePropertyMatcher") {
    book should have (
      title ("Programming in Scala"),
      author (List("Odersky", "Spoon", "Venners")),
      pubYear (2008)
    )
  }
