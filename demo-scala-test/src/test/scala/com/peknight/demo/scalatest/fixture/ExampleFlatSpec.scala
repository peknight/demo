package com.peknight.demo.scalatest.fixture

import org.scalatest.flatspec.AnyFlatSpec
import scala.collection.mutable.ListBuffer

class ExampleFlatSpec extends AnyFlatSpec:
  // def fixture =
  //   new {
  //     val builder = new StringBuilder("ScalaTest is ")
  //     val buffer = new ListBuffer[String]
  //   }

  // "Testing" should "be easy" in {
  //   val f = fixture
  //   f.builder.append("easy!")
  //   assert(f.builder.toString === "ScalaTest is easy!")
  //   assert(f.buffer.isEmpty)
  //   f.buffer += "sweet"
  // }
  // it should "be fun" in {
  //   val f = fixture
  //   f.builder.append("fun!")
  //   assert(f.builder.toString === "ScalaTest is fun!")
  //   assert(f.buffer.isEmpty)
  // }

  trait Builder:
    val builder = new StringBuilder("ScalaTest is ")

  trait Buffer:
    val buffer = ListBuffer("ScalaTest", "is")

  // This test needs the StringBuilder fixture
  "Test" should "be productive" in new Builder {
    builder.append("productive!")
    assert(builder.toString === "ScalaTest is productive!")
  }

  // This test needs the ListBuffer[String] fixture
  "Test code" should "be readable" in new Buffer {
    buffer += ("readable!")
    assert(buffer === List("ScalaTest", "is", "readable!"))
  }

  it should "be clear and concise" in new Builder with Buffer {
    builder.append("clear!")
    buffer += ("concise!")
    assert(builder.toString === "ScalaTest is clear!")
    assert(buffer === List("ScalaTest", "is", "concise!"))
  }