package com.peknight.demo.scalatest.privatemethod

import org.scalatest.PrivateMethodTester
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class PrivateMethodTesterFunSuite extends AnyFunSuite with PrivateMethodTester:
  class PrivateMethodObject:
    private[this] def decorateToStringValue(i: Int): String = s"$i!"
    private[this] def privateParameterlessMethod: Int = 16
  end PrivateMethodObject

  val decorateToStringValue = PrivateMethod[String](Symbol("decorateToStringValue"))
  val privateParameterlessMethod = PrivateMethod[Int](Symbol("privateParameterlessMethod"))

  test("Using PrivateMethodTester") {
    val targetObject = new PrivateMethodObject
    val decorateResult = targetObject invokePrivate decorateToStringValue(1)
    val parameterlessResult = targetObject invokePrivate privateParameterlessMethod()
    decorateResult shouldBe "1!"
    parameterlessResult shouldBe 16
  }
