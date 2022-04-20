package com.peknight.demo.cats.monad

import cats.Monad

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object FutureApp:

  def doSomethingLongRunning: Future[Int] = Future(1)
  def doSomethingElseLongRunning: Future[Int] = Future(2)

  def doSomethingVeryLongRunning: Future[Int] =
    for
      result1 <- doSomethingLongRunning
      result2 <- doSomethingElseLongRunning
    yield result1 + result2

  def main(args: Array[String]): Unit = {
    println(Await.result(doSomethingVeryLongRunning, 1.second))
    val fm = Monad[Future]
    val x = fm.pure(1)
    val future = fm.flatMap(fm.pure(1)) {
      x => fm.pure(x + 2)
    }
    println(Await.result(future, 1.second))
  }
