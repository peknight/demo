package com.peknight.demo.catsstm.datatypes

import cats.effect.{IO, IOApp}
import cats.syntax.semigroup.*
import io.github.timwspence.cats.stm.STM

object TQueueApp extends IOApp.Simple:

  val run: IO[Unit] =
    for
      stm <- STM.runtime[IO]
      result <- stm.commit {
        for
          tqueue <- stm.TQueue.empty[String]
          _ <- tqueue.put("hello")
          _ <- tqueue.put("world")
          hello <- tqueue.read
          world <- tqueue.peek
        yield hello |+| world
      }
      _ <- IO.println(result)
    yield ()
