package com.peknight.demo.scalatest.fixture

import org.scalatest.Outcome
import org.scalatest.flatspec.FixtureAnyFlatSpec

import java.io.{File, FileWriter}

class ExampleFixtureFlatSpec extends FixtureAnyFlatSpec:
  case class FixtureParam(file: File, writer: FileWriter)
  def withFixture(test: OneArgTest): Outcome =
    // create the fixture
    val file = File.createTempFile("hello", "world")
    val writer = new FileWriter(file)
    val theFixture = FixtureParam(file, writer)
    try
      // set up the fixture
      writer.write("ScalaTest is ")
      // "loan" the fixture to the test
      withFixture(test.toNoArgTest(theFixture))
    // clean up the fixture
    finally writer.close()

  "Testing" should "be easy" in { f =>
    f.writer.write("easy!")
    f.writer.flush()
    assert(f.file.length === 18)
  }

  it should "be fun" in { f =>
    f.writer.write("fun!")
    f.writer.flush()
    assert(f.file.length === 17)
  }