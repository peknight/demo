package com.peknight.demo.catseffect.standard

import cats.effect.{IO, IOApp, Temporal}
import cats.effect.std.{Console, Semaphore}
import cats.effect.syntax.all._
import cats.syntax.all._

import scala.concurrent.duration.DurationInt

object SemaphoreApp extends IOApp.Simple {
  class PreciousResource[F[_]: Temporal](name: String, s: Semaphore[F])(implicit F: Console[F]) {
    def use: F[Unit] = for {
      x <- s.available
      _ <- F.println(s"$name >> Availability: $x")
      _ <- s.acquire
      y <- s.available
      _ <- F.println(s"$name >> Started | Availability: $y")
      _ <- s.release.delayBy(3.seconds)
      z <- s.available
      _ <- F.println(s"$name >> Done | Availability: $z")
    } yield ()
  }

  val program: IO[Unit] = for {
    s <- Semaphore[IO](1)
    r1 = new PreciousResource[IO]("R1", s)
    r2 = new PreciousResource[IO]("R2", s)
    r3 = new PreciousResource[IO]("R3", s)
    _ <- List(r1.use, r2.use, r3.use).parSequence.void
  } yield ()

  val run = program
}
