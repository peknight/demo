package com.peknight.demo.cats.effect.io

import cats.effect.IO

object IOApp extends App {

  // Introduction

  val ioa = IO { println("hey!") }

  val program: IO[Unit] = for {
    _ <- ioa
    _ <- ioa
  } yield ()
  program.unsafeRunSync()

  // On Referential Transparency and Lazy Evaluation

  def addToGauge(value: Int): IO[Unit] = IO { println(value) }

  (for {
    _ <- addToGauge(32)
    _ <- addToGauge(32)
  } yield ()).unsafeRunSync()

  val task = addToGauge(32)

  (for {
    _ <- task
    _ <- task
  } yield ()).unsafeRunSync()

  // Stack Safety

  def fib(n: Int, a: Long = 0, b: Long = 1): IO[Long] =
    IO(a + b).flatMap { b2 =>
      if (n > 0) fib(n - 1, b, b2)
      else IO.pure(a)
    }

  println(fib(5).unsafeRunSync())

  // Describing Effects

  // Pure Values -- IO.pure & IO.unit

  // passed "by value"
  IO.pure(25).flatMap(n => IO(println(s"Number is :$n"))).unsafeRunSync()

  IO.pure(println("THIS IS WRONG!"))

  // IO.unit is simply an alias for IO.pure(())
  val unit: IO[Unit] = IO.pure(())

  // Synchronous Effects -- IO.apply

  // IO.apply passed "by name"
  def putStrLn(value: String) = IO(println(value))
  val readLn = IO(scala.io.StdIn.readLine())

  (for {
    _ <- putStrLn("What's your name? (need input)")
    n <- readLn
    _ <- putStrLn(s"Hello, $n!")
  } yield ()).unsafeRunSync()

}
