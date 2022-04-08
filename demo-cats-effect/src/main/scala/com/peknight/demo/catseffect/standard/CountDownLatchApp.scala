package com.peknight.demo.catseffect.standard

import cats.effect.std.CountDownLatch
import cats.effect.{IO, IOApp}

object CountDownLatchApp extends IOApp.Simple {
  val run = for {
    c <- CountDownLatch[IO](2)
    f <- (c.await >> IO.println("Countdown latch unblocked")).start
    _ <- c.release
    _ <- IO.println("Before latch is unblocked")
    _ <- c.release
    _ <- f.join
  } yield ()
}
