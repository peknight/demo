package com.peknight.demo.cats.monad

import cats.Monad

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object FutureApp extends App {

  def doSomethingLongRunning: Future[Int] = ???
  def doSomethingElseLongRunning: Future[Int] = ???

  def doSomethingVeryLongRunning: Future[Int] =
    for {
      result1 <- doSomethingLongRunning
      result2 <- doSomethingElseLongRunning
    } yield result1 + result2

  val fm = Monad[Future]

  val future = fm.flatMap(fm.pure(1))(x => fm.pure(x + 2))
  println(Await.result(future, 1.second))
}
