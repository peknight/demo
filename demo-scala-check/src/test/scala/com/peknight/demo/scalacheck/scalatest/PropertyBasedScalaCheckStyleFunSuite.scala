package com.peknight.demo.scalacheck.scalatest

import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.Checkers
import org.scalacheck.Prop.*

class PropertyBasedScalaCheckStyleFunSuite extends AnyFunSuite with Checkers:
  test("native ScalaCheck style") {
    check((n: Int) => n > 1 ==> n / 2 > 0)
  }
