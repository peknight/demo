package com.peknight.demo.scalatest.style

import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.GivenWhenThen

/**
 * The FeatureSpec style
 *
 * The FeatureSpec style is primarily intended for acceptance testing, including facilitating the process of programmers
 * working alongside non-programmers to define the acceptance requirements.
 *
 * To select just the FeatureSpec style in an sbt build, including this line:
 * libraryDependencies += "org.scalatest" %% "scalatest-featurespec" % version % Test
 */
class TVSetFeatureSpec extends AnyFeatureSpec with GivenWhenThen:
  info("As a TV set owner")
  info("I want to be able to turn the TV on and off")
  info("So I can watch TV when I want")
  info("And save energy when I'm not watching TV")

  Feature("TV power button") {
    Scenario("User presses power button when TV is off") {
      Given("a TV set that is switched off")
      val tv = new TVSet
      assert(!tv.isOn)

      When("the power button is pressed")
      tv.pressPowerButton()

      Then("the TV should switch on")
      assert(tv.isOn)
    }

    Scenario("User presses power button when TV is on") {
      Given("a TV set that is switched on")
      val tv = new TVSet
      tv.pressPowerButton()
      assert(tv.isOn)

      When("the power button is pressed")
      tv.pressPowerButton()

      Then("the TV should switch off")
      assert(!tv.isOn)
    }
  }
