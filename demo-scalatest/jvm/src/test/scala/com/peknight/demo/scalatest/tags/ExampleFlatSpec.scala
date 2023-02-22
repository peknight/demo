package com.peknight.demo.scalatest.tags

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.tagobjects.Slow

class ExampleFlatSpec extends AnyFlatSpec:
  "The Scala language" must "add correctly" taggedAs(Slow) in {
    val sum = 1 + 1
    assert(sum === 2)
  }
  it must "subtract correctly" taggedAs(Slow, DbTest) in {
    val diff = 4 - 1
    assert(diff === 3)
  }
