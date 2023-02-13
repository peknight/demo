package com.peknight.demo.scalacheck.scalatest

import com.peknight.demo.scalacheck.scalatest.Action
import com.peknight.demo.scalacheck.scalatest.Action.{Click, Enter, Start}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class MutableObjectsFunSuite extends AnyFunSuite with ScalaCheckPropertyChecks:

  val stateTransitions =
    Table(
      ("action", "expectedCount"),
      (Start, 0),
      (Click, 1),
      (Click, 2),
      (Click, 3),
      (Enter(5), 5),
      (Click, 6),
      (Enter(1), 1),
      (Click, 2),
      (Click, 3)
    )

  test("check the actual value equals the expected value") {
    val counter = new Counter
    forAll (stateTransitions) { (action, expectedCount) =>
      action match
        case Start => counter.reset()
        case Click => counter.click()
        case Enter(n) => counter.enter(n)
      counter.count should equal (expectedCount)
    }
  }
