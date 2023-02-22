package com.peknight.demo.scalatest.matcher

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import com.peknight.demo.scalatest.matcher.FileEndsWithExtensionMatchers.*

class CustomMatchersFunSuite extends AnyFunSuite:
  test("using custom matchers") {
    val file = new java.io.File("build.sbt")
    file should endWithExtension ("sbt")
    // file should not endWithExtension "txt"
    // file should (exist and endWithExtension ("sbt"))
  }

