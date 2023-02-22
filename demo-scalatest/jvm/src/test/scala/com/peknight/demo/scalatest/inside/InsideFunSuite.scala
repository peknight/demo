package com.peknight.demo.scalatest.inside

import org.scalatest.Inside.inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class InsideFunSuite extends AnyFunSuite:
  val rec = Record(Name("Sally", "Anna", "Jones"), Address("25 Main St", "Los Angeles", "CA", "12345"), 38)
  test("make statements abount nested object graphs using pattern matching") {
    inside (rec) { case Record(name, address, age) =>
      inside (name) { case Name(first, middle, last) =>
        first should be ("Sally")
        middle should be ("Anna")
        last should be ("Jones")
      }
      inside (address) { case Address(street, city, state, zip) =>
        street should startWith ("25")
        city should endWith ("Angeles")
        state should equal ("CA")
        zip should be ("12345")
      }
      age should be < 99
    }
  }
