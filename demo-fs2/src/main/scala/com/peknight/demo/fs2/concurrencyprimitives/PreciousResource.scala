package com.peknight.demo.fs2.concurrencyprimitives

import cats.effect.Temporal
import cats.effect.std.{Console, Semaphore}
import cats.implicits.toFlatMapOps
import fs2.Stream

import scala.concurrent.duration.DurationInt

class PreciousResource[F[_]: Temporal: Console](name: String, s: Semaphore[F]):
  def use: Stream[F, Unit] =
    for
      _ <- Stream.eval(s.available.flatMap(a => Console[F].println(s"$name >> Availability: $a")))
      _ <- Stream.eval(s.acquire)
      _ <- Stream.eval(s.available.flatMap(a => Console[F].println(s"$name >> Started | Availability: $a")))
      _ <- Stream.sleep(3.seconds)
      _ <- Stream.eval(s.release)
      _ <- Stream.eval(s.available.flatMap(a => Console[F].println(s"$name >> Done | Availability: $a")))
    yield ()
