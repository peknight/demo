package com.peknight.demo.fs2.concurrencyprimitives

import cats.effect.std.Semaphore
import cats.effect.{IO, IOApp}
import fs2.Stream

object SharedResourceApp extends IOApp.Simple:
  val stream =
    for
      s <- Stream.eval(Semaphore[IO](1))
      r1 = new PreciousResource[IO]("R1", s)
      r2 = new PreciousResource[IO]("R2", s)
      r3 = new PreciousResource[IO]("R3", s)
      _ <- Stream(r1.use, r2.use, r3.use).parJoin(3)
    yield ()

  val run = stream.compile.drain
