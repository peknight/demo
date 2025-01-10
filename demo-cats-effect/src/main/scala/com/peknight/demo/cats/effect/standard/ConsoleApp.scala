package com.peknight.demo.cats.effect.standard

import cats.effect.std.Console
import cats.effect.{IO, IOApp}

import scala.concurrent.duration.*

object ConsoleApp extends IOApp.Simple:
  val run: IO[Unit] =
    for
      _ <- Console[IO].println("Please enter your name: ")
      n <- Console[IO].readLine
      _ <- if n.nonEmpty then Console[IO].println(s"Hello, $n") else Console[IO].errorln("Name is empty!")
      /*
       * The `readLine` is not always cancelable on JVM, for reasons outside our control.
       * For example, the following code will deadlock:
       */
      s <- IO.readLine.timeoutTo(3.seconds, IO.pure("hello"))
      _ <- IO.println(s)
    yield ()

