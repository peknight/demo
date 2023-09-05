package com.peknight.demo.catseffect.standard

import cats.effect.{IO, IOApp}
import cats.effect.std.Supervisor

object SupervisorApp extends IOApp.Simple:
  val run: IO[Unit] =
    for
      res <- Supervisor[IO](await = true).use(supervisor => supervisor.supervise(IO.never).void)
      _ <- IO.println(res)
    yield ()
