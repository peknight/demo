package com.peknight.demo.scalacheck.scalatest

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class SupplyingArgumentNamesFunSuite extends AnyFunSuite with ScalaCheckPropertyChecks:
  test("specify string names for the arguments passed to a property function") {
    forAll ("a", "b") { (a: String, b: String) =>
      a.length + b.length should equal ((a + b).length + 0)
    }
  }
