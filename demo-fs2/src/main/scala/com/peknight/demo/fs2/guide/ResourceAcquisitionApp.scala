package com.peknight.demo.fs2.guide

import cats.effect.{IO, IOApp}
import com.peknight.demo.fs2.guide.ErrorHandlingApp.err
import fs2.Stream

import java.util.concurrent.atomic.AtomicLong

object ResourceAcquisitionApp extends IOApp.Simple:
  val count = new AtomicLong(0)
  val acquire = IO(count.incrementAndGet()).flatMap(l => IO.println(s"incremented: $l"))
  val release = IO(count.decrementAndGet()).flatMap(l => IO.println(s"decremented: $l"))

  val run =
    for
      _ <- Stream.bracket(acquire)(_ => release).flatMap(_ => Stream(1, 2, 3) ++ err)
        .handleErrorWith(t => Stream.eval(IO.println(t).map(_ => 0)))
        .compile.toList.flatMap(IO.println)
      _ <- IO(count.get()).flatMap(IO.println)
    yield ()
