package com.peknight.demo.scalatest.partial

import org.scalatest.PartialFunctionValues.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class PartialFunctionFunSuite extends AnyFunSuite:
  test("if the partial function wasn't defined at that input, it would throw some exception") {
    val pf: PartialFunction[String, Int] = Map("I" -> 1, "II" -> 2, "III" -> 3, "IV" -> 4)
    assert(pf.valueAt("IV") === 4)
    // pf.isDefinedAt("V") should be (true)
    pf.isDefinedAt("IV") should be (true)
    // pf("V") should equal (5)
    pf("IV") should equal (4)
    // pf.valueAt("V") should equal (5)
    pf.valueAt("IV") should equal(4)
  }
