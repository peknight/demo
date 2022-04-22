package com.peknight.demo.scala3.assertionstests

import org.scalatest.*
import org.scalatest.featurespec.AnyFeatureSpec

class TVSetSpec extends AnyFeatureSpec, GivenWhenThen:
  Feature("TV power button") {
    Scenario("User presses power button when TV is off") {
      Given("a TV set that is switched off")
      When("the power button is pressed")
      Then("the TV should switch on")
      pending
    }
  }
