package com.peknight.demo.cats.casestudy.mapreduce

import cats.Monad
import cats.kernel.Monoid
import cats.syntax.traverse._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object FutureApp extends App {
  val future1 = Future {
    (1 to 100).toList.foldLeft(0)(_ + _)
  }

  val future2 = Future {
    (100 to 200).toList.foldLeft(0)(_ + _)
  }

  val future3 = future1.map(_.toString)

  val future4 = for {
    a <- future1
    b <- future2
  } yield a + b

//  Future.sequence(List(Future(1), Future(2), Future(3)))
  List(Future(1), Future(2), Future(3)).sequence

  println(Await.result(Future(1), 1.second))

  Monad[Future].pure(42)

  println(Await.result(Monoid[Future[Int]].combine(Future(1), Future(2)), 1.second))

  println(Runtime.getRuntime.availableProcessors())

  (1 to 10).toList.grouped(3).toList
}
