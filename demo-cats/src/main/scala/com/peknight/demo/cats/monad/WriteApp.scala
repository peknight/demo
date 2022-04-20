package com.peknight.demo.cats.monad

import cats.data.Writer
import cats.syntax.applicative.*
import cats.syntax.writer.*

import scala.concurrent.*
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.*

object WriteApp extends App:
  println(Writer(Vector(
    "It was the best of times",
    "it was the worst of times"
  ), 1859))

  type Logged[A] = Writer[Vector[String], A]
  println(123.pure[Logged])

  println(Vector("msg1", "msg2", "msg3").tell)

  val a = Writer(Vector("msg1", "msg2", "msg3"), 123)
  val b = 123.writer(Vector("msg1", "msg2", "msg3"))

  val aResult: Int = a.value
  val aLog: Vector[String] = a.written
  println(aResult)
  println(aLog)
  val (log, result) = b.run
  println(result)
  println(log)

  val writer1 =
    for
      a <- 10.pure[Logged]
      _ <- Vector("a", "b", "c").tell
      b <- 32.writer(Vector("x", "y", "z"))
    yield a + b
  println(writer1.run)

  println(10.pure[Logged].flatMap(a =>
    Vector("a", "b", "c").tell.flatMap(_ =>
      32.writer(Vector("x", "y", "z")).map(b =>
        a + b
      )
    )
  ).run)

  val writer2 = writer1.mapWritten(_.map(_.toUpperCase))
  println(writer2.run)

  val writer3 = writer1.bimap(
    log => log.map(_.toUpperCase),
    res => res * 100
  )
  println(writer3.run)

  val writer4 = writer1.mapBoth { (log, res) =>
    val log2 = log.map(_ + "!")
    val res2 = res * 1000
    (log2, res2)
  }
  println(writer4.run)

  val writer5 = writer1.reset
  println(writer5.run)

  val writer6 = writer1.swap
  println(writer6.run)

  def slowly[A](body: => A) = try body finally Thread.sleep(100)

  def factorial(n: Int): Int =
    val ans = slowly(if n == 0 then 1 else n * factorial(n - 1))
    println(s"fact $n $ans")
    ans

  println(factorial(5))

  println(Await.result(Future.sequence(Vector(
    Future(factorial(5)),
    Future(factorial(5))
  )), 5.seconds))

  def factorialWithLog(n: Int): Logged[Int] =
    for
      ans <- if n == 0 then 1.pure[Logged] else slowly(factorialWithLog(n - 1).map(_ * n))
      _ <- Vector(s"fact $n $ans").tell
    yield ans

  val (logFactorial, res) = factorialWithLog(5).run
  println(logFactorial)
  println(res)

  println(Await.result(Future.sequence(Vector(
    Future(factorialWithLog(5)),
    Future(factorialWithLog(5))
  )), 5.seconds))
