package com.peknight.demo.fs2.concurrencyprimitives

import cats.effect.{IO, IOApp}
import fs2.Stream
import fs2.concurrent.SignallingRef

import scala.concurrent.duration.DurationInt

object SignalApp extends IOApp.Simple:

  val run =
    for
      _ <- SignallingRef[IO, Boolean](false).flatMap { signal =>
        val s1 = Stream.awakeEvery[IO](1.second).interruptWhen(signal)
        val s2 = Stream.sleep[IO](4.seconds) >> Stream.eval(signal.set(true))
        s1.concurrently(s2).compile.toVector
      }.flatMap(IO.println)
    yield ()
