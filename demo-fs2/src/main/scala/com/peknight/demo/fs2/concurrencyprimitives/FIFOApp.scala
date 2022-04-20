package com.peknight.demo.fs2.concurrencyprimitives

import cats.effect.std.Queue
import cats.effect.{IO, IOApp}
import fs2.Stream

import scala.concurrent.duration.DurationInt

object FIFOApp extends IOApp.Simple:

  val stream =
    for
      q1 <- Stream.eval(Queue.bounded[IO, Int](1))
      q2 <- Stream.eval(Queue.bounded[IO, Int](100))
      bp = new Buffering[IO](q1, q2)
      _ <- Stream.sleep[IO](5.seconds).concurrently(bp.start.drain)
    yield ()

  val run = stream.compile.drain