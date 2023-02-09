package com.peknight.demo.scalatest.fixture

import org.scalatest.{TestSuite, TestSuiteMixin}

import scala.collection.mutable.ListBuffer

trait Buffer extends TestSuiteMixin { this: TestSuite =>
  val buffer = new ListBuffer[String]
  abstract override def withFixture(test: NoArgTest) =
    // To be stackable, must call super.withFixture
    try super.withFixture(test)
    finally buffer.clear()

}
