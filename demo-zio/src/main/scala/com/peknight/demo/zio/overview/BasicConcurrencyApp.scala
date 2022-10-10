package com.peknight.demo.zio.overview

import zio.*

import java.io.IOException

object BasicConcurrencyApp extends ZIOAppDefault:

  def fib(n: Long): UIO[Long] = ZIO.suspendSucceed {
    // 性能极差
    if n <= 1 then ZIO.succeed(n) else fib(n - 1).zipWith(fib(n - 2))(_ + _)
  }

  // Forking Effects
  val fib100Fiber: UIO[Fiber[Nothing, Long]] =
    for
      fiber <- fib(100).fork
    yield fiber

  // Joining Fibers
  val joiningFibers: UIO[String] =
    for
      fiber <- ZIO.succeed("Hi!").fork
      message <- fiber.join
    yield message

  // Awaiting Fibers
  val awaitingFibers: UIO[Exit[Nothing, String]] =
    for
      fiber <- ZIO.succeed("Hi!").fork
      exit <- fiber.await
    yield exit

  // Interrupting Fibers
  val interruptingFibers: UIO[Exit[Nothing, Nothing]] =
    for
      fiber <- ZIO.succeed("Hi!").forever.fork
      /*
       * `Fiber#interrupt` dose not resume until the fiber has completed
       * which helps ensure your code does not spin up new fibers until the old one has terminated.
       */
      exit <- fiber.interrupt
      /*
       * If this behavior (often called "back-pressuring") is not desired,
       * you can `ZIO#fork` the interruption itself into a new fiber
       */
      // _ <- fiber.interrupt.fork
    yield exit

  // Composing Fibers
  val composingFibers1: UIO[(String, String)] =
    for
      fiber1 <- ZIO.succeed("Hi!").fork
      fiber2 <- ZIO.succeed("Bye!").fork
      fiber = fiber1.zip(fiber2)
      tuple <- fiber.join
    yield tuple

  val composingFibers2: UIO[String] =
    for
      fiber1 <- ZIO.fail("Uh oh!").fork
      fiber2 <- ZIO.succeed("Hurray!").fork
      fiber = fiber1.orElse(fiber2)
      message <- fiber.join
    yield message

  // Racing
  val racing: UIO[String] = ZIO.succeed("Hello").race(ZIO.succeed("Goodbye"))

  // Timeout
  val timeout: UIO[Option[String]] = ZIO.succeed("Hello").timeout(10.seconds)

  def run: IO[IOException, Unit] =
    for
      message <- joiningFibers
      _ <- Console.printLine(message)
      awaitingExit <- awaitingFibers
      _ <- Console.printLine(awaitingExit)
      interruptingExit <- interruptingFibers
      _ <- Console.printLine(interruptingExit)
      composingTuple <- composingFibers1
      _ <- Console.printLine(composingTuple)
      composingMessage <- composingFibers2
      _ <- Console.printLine(composingMessage)
      racingMessage <- racing
      _ <- Console.printLine(racingMessage)
      timeoutMessage <- timeout
      _ <- Console.printLine(timeoutMessage)
      _ <- Console.printLine("Bye!")
    yield ()