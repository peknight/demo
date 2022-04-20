package com.peknight.demo.catseffect.concurrent

import cats.effect.{IO, IOApp}

object MemoizationApp extends IOApp.Simple:

  val action: IO[String] = IO.println("This is only printed once").as("action")

  val x: IO[String] =
    for
      memoized <- action.memoize
      res1 <- memoized
      res2 <- memoized
    yield res1 ++ res2

  val run =
    for
      res <- x
      _ <- IO.println(res)
    yield ()
