package com.peknight.demo.scalatest.async

import org.scalatest.flatspec.AsyncFlatSpec

import scala.concurrent.Future

class AddFlatSpec extends AsyncFlatSpec:
  def addSoon(addends: Int*): Future[Int] = Future { addends.sum }

  behavior of "addSoon"

  it should "eventually compute a sum of passed Ints" in {
    val futureSum: Future[Int] = addSoon(1, 2)
    // You can map assertions onto a Future, then return
    // the resulting Future[Assertion] to ScalaTest
    futureSum map { sum => assert(sum == 3) }
  }

  def addNow(addends: Int*): Int = addends.sum

  "addNow" should "immediately compute a sum of passed Ints" in {
    val sum: Int = addNow(1, 2)
    // You can also write synchronous tests. The body
    // must have result type Assertion:
    assert(sum == 3)
    println("hi")
    succeed
  }

  "IllegalStateException" should "recover to succeeded" in {
    recoverToSucceededIf[IllegalStateException] {
      // Future { 42 }
      Future { throw new IllegalStateException }
    }
  }

