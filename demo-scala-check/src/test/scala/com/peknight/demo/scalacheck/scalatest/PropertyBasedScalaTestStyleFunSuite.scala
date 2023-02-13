package com.peknight.demo.scalacheck.scalatest

import org.scalacheck.Prop.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class PropertyBasedScalaTestStyleFunSuite extends AnyFunSuite with ScalaCheckDrivenPropertyChecks:
  test("ScalaTest property style") {
    forAll( (n: Int) => whenever (n > 1) {
      n / 2 should be > 0
    })
  }
