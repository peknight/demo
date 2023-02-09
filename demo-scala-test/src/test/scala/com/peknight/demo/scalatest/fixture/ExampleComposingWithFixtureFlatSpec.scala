package com.peknight.demo.scalatest.fixture

import org.scalatest.flatspec.AnyFlatSpec

class ExampleComposingWithFixtureFlatSpec extends AnyFlatSpec with Builder with Buffer:
  "Testing" should "be easy" in {
    builder.append("easy!")
    assert(builder.toString === "ScalaTest is easy!")
    assert(buffer.isEmpty)
    buffer += "sweet"
  }

  it should "be fun" in {
    builder.append("fun!")
    assert(builder.toString === "ScalaTest is fun!")
    assert(buffer.isEmpty)
    buffer += "clear"
  }
