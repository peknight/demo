package com.peknight.demo.cats.effect.standard

import cats.effect.{IO, IOApp}
import cats.effect.std.Backpressure

import scala.concurrent.duration.*

object BackpressureApp extends IOApp.Simple:
  val program =
    for
      backpressure <- Backpressure[IO](Backpressure.Strategy.Lossless, 1)
      f1 <- backpressure.metered(IO.sleep(1.second) *> IO.pure(1) <* IO.println("f1 done.")).start
      f2 <- backpressure.metered(IO.sleep(1.second) *> IO.pure(1) <* IO.println("f2 done.")).start
      res1 <- f1.joinWithNever
      res2 <- f2.joinWithNever
    yield (res1, res2)

  val run: IO[Unit] =
    for
      _ <- IO.println("start...")
      res <- program
      _ <- IO.println(res)
    yield ()


