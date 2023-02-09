package com.peknight.demo.scalatest.matcher

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

import java.io.File

class CheckingBooleanPropertiesFunSuite extends AnyFunSuite with FileDirectoryMatchers:
  test("iter should be traversableAgain") {
    val iter = List(1, 2, 3)
    iter shouldBe Symbol("traversableAgain")
  }

  test("temp should be a file") {
    val temp = new File("build.sbt")
    temp should be a Symbol("file")
  }

  test("temp should be a file, not a directory (BePropertyMatcher)") {
    val temp = File.createTempFile("delete", "me")
    try
      temp should be a (file)
      temp should not be a (directory)
    finally temp.delete()
  }

  // keyEvent should be an Symbol("actionKey")

