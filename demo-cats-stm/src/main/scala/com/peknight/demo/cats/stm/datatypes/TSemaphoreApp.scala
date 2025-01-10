package com.peknight.demo.cats.stm.datatypes

import cats.effect.{IO, IOApp}
import io.github.timwspence.cats.stm.STM

object TSemaphoreApp extends IOApp.Simple:
  val run: IO[Unit] =
    for
      stm <- STM.runtime[IO]
      result <- stm.commit {
        for
          tsem <- stm.TSemaphore.make(1)
          _ <- tsem.acquire
          zero <- tsem.available
          _ <- tsem.release
        yield zero
      }
      _ <- IO.println(result)
    yield ()
