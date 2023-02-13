package com.peknight.demo.scalatest.matcher

import org.scalatest.LoneElement.convertToCollectionLoneElementWrapper
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class SingleElementCollectionsFunSuite extends AnyFunSuite:
  test("a set should contain just one element, as Int less than or equal to 10") {
    val set = Set(10)
    set.loneElement should be <= 10
  }
