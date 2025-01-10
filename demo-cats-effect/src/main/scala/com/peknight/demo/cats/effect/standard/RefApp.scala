package com.peknight.demo.cats.effect.standard

import cats.effect.{IO, IOApp, Ref, Sync}
import cats.syntax.all.*

object RefApp extends IOApp.Simple:
  class Worker[F[_]](number: Int, ref: Ref[F, Int])(using F: Sync[F]):
    private def putStrLn(value: String): F[Unit] = F.delay(println(value))
    def start: F[Unit] =
      for
        c1 <- ref.get
        _ <- putStrLn(show"#$number >> $c1")
        c2 <- ref.modify(x => (x + 1, x))
        _ <- putStrLn(show"#$number >> $c2")
      yield ()

  val program: IO[Unit] =
    for
      ref <- Ref[IO].of(0)
      w1 = new Worker[IO](1, ref)
      w2 = new Worker[IO](2, ref)
      w3 = new Worker[IO](3, ref)
      _ <- List(w1.start, w2.start, w3.start).parSequence.void
      res <- ref.get
      _ <- IO.println(s"final value:$res")
    yield ()

  val run = program
