package com.peknight.demo.scalatest.matcher

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class CheckingStringsFunSuite extends AnyFunSuite:

  test("whether a string starts with, ends with, or includes a substring") {
    val string = "Hello seven world"
    string should startWith ("Hello")
    string should endWith ("world")
    string should include ("seven")
  }

  test("whether a string starts with, ends with, or includes a regular expression") {
    val string = "Hello world"
    string should startWith regex "Hel*o"
    string should endWith regex "wo.ld"
    string should include regex "wo.ld"
  }

  test("whether a string fully matches a regular expression") {
    val string = "3.1415926535897932384626"
    string should fullyMatch regex """(-)?(\d+)(\.\d*)?"""
  }

  test("with an optional specification of required groups") {
    "abbccxxx" should startWith regex ("a(b*)(c*)" withGroups ("bb", "cc"))
    "xxxabbcc" should endWith regex ("a(b*)(c*)" withGroups ("bb", "cc"))
    "xxxabbccxxx" should include regex ("a(b*)(c*)" withGroups ("bb", "cc"))
    "abbcc" should fullyMatch regex ("a(b*)(c*)" withGroups ("bb", "cc"))
  }