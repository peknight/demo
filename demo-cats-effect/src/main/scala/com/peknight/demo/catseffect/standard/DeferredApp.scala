package com.peknight.demo.catseffect.standard

import cats.effect.{Deferred, IO, IOApp}
import cats.implicits.{catsSyntaxParallelSequence1, showInterpolator}

object DeferredApp extends IOApp.Simple {
  def start(d: Deferred[IO, Int]): IO[Unit] = {
    val attemptCompletion: Int => IO[Unit] = n => d.complete(n).attempt.void
    List(
      IO.race(attemptCompletion(1), attemptCompletion(2)),
      d.get.flatMap { n => IO.println(show"Result: $n") }
    ).parSequence.void
  }

  val program: IO[Unit] = for {
    d <- Deferred[IO, Int]
    _ <- start(d)
  } yield ()

  val run = program
}
