package com.peknight.demo.scalatest.fixture

import org.scalatest.{BeforeAndAfterEach, Suite}

import scala.collection.mutable.ListBuffer

trait BufferBeforeAndAfterEach extends BeforeAndAfterEach { this: Suite =>
  val buffer = new ListBuffer[String]

  override def afterEach(): Unit =
    try super.afterEach()
    finally buffer.clear()
}
