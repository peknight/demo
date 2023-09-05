package com.peknight.demo.catseffect.standard

import cats.effect.{IO, IOApp}
import cats.effect.std.Env

object EnvApp extends IOApp.Simple:
  val program: IO[Unit] =
    for
      variable <- Env[IO].get("MY_VARIABLE")
      _ <- IO.println(s"The variable is [$variable]")
    yield ()

  val run = program
