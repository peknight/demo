package com.peknight.demo.scalatest.fixture

import org.scalatest.{TestSuite, TestSuiteMixin}

trait Builder extends TestSuiteMixin { this: TestSuite =>

  val builder = new StringBuilder

  abstract override def withFixture(test: NoArgTest) =
    builder.append("ScalaTest is ")
    // To be stackable, must call super.withFixture
    try super.withFixture(test)
    finally builder.clear()

}
