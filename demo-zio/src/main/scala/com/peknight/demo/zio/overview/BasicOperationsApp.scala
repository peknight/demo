package com.peknight.demo.zio.overview

import zio.*

import java.io.IOException

object BasicOperationsApp:
  // Mapping
  val succeeded: UIO[Int] = ZIO.succeed(21).map(_ * 2)
  val failed: IO[Exception, Unit] = ZIO.fail("No no!").mapError(msg => new Exception(msg))

  // Chaining
  val sequenced: IO[IOException, Unit] =
    Console.readLine.flatMap(input => Console.printLine(s"You entered: $input"))

  // For Comprehensions
  val program: IO[IOException, Unit] =
    for
      _ <- Console.printLine("Hello! What is your name?")
      name <- Console.readLine
      _ <- Console.printLine(s"Hello, $name, welcome to ZIO!")
    yield ()

  // Zipping
  val zipped: UIO[(String, Int)] = ZIO.succeed("4").zip(ZIO.succeed(2))

  val zipRight1: IO[IOException, String] = Console.printLine("What is your name?").zipRight(Console.readLine)
  val zipRight2: IO[IOException, String] = Console.printLine("What is your name?") *> Console.readLine
