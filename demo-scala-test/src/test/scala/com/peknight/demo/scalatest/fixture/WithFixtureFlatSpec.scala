package com.peknight.demo.scalatest.fixture

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.{Failed, Outcome}

import java.io.File

class WithFixtureFlatSpec extends AnyFlatSpec:
  override def withFixture(test: NoArgTest): Outcome =
    super.withFixture(test) match
      case failed: Failed =>
        val currDir = new File(".")
        val fileNames = currDir.list()
        info(s"Dir snapshot: ${fileNames.mkString(", ")}")
        failed
      case other => other

  "This test" should "succeed" in {
    assert(1 + 1 === 2)
  }

  it should "fail" in {
    // assert(1 + 1 === 3)
    assert(1 + 1 === 2)
  }