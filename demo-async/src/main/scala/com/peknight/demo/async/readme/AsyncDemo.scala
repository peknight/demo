package com.peknight.demo.async.readme

import scala.async.Async.{async, await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object AsyncDemo extends App {

  def slowCalcFuture: Future[Int] = Future {
    println("start!")
    Thread.sleep(100)
    println("finish!")
    42
  }             // 01

  def combinedNotParallel: Future[Int] = async {               // 02
    await(slowCalcFuture) + await(slowCalcFuture)   // 03
  }

  def combined: Future[Int] = async {
    val future1 = slowCalcFuture
    val future2 = slowCalcFuture
    await(future1) + await(future2)
  }

  val x: Int = Await.result(combined, 10.seconds)   // 05

  println(x)
}
