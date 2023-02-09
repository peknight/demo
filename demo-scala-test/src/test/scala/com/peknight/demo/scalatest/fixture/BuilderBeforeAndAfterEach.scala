package com.peknight.demo.scalatest.fixture

import org.scalatest.{BeforeAndAfterEach, Suite}

trait BuilderBeforeAndAfterEach extends BeforeAndAfterEach { this: Suite =>
  val builder = new StringBuilder

  override def beforeEach(): Unit =
    // To be stackable, must call super.beforeEach
    builder.append("ScalaTest is ")
    super.beforeEach()

  override def afterEach(): Unit =
    // To be stackable, must call super.afterEach
    try super.afterEach()
    finally builder.clear()
}
