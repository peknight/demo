package com.peknight.demo.fs2.guide

import cats.effect.{Clock, IO, IOApp}
import fs2.Stream

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.DurationInt

object ConcurrencyApp extends IOApp.Simple:

  val run =
    for
      start <- Clock[IO].monotonic
      // 如果不在IOApp上下文使用（如：unsafeRunSync()）可以`import cats.effect.unsafe.implicits.global`
      _ <- Stream(1, 2, 3).merge(Stream.eval(IO.sleep(200.millis).as(4))).compile.toVector.flatMap(IO.println)
      end <- Clock[IO].monotonic
      _ <- IO.println((end - start).toUnit(TimeUnit.MILLISECONDS))
    yield ()
