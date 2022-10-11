package com.peknight.demo.catsstm.datatypes

import cats.effect.{IO, IOApp}
import cats.syntax.semigroup.*
import io.github.timwspence.cats.stm.STM

object TMVarApp extends IOApp.Simple:

  val run: IO[Unit] =
    for
      stm <- STM.runtime[IO]
      result <- stm.commit {
        for
          tmvar <- stm.TMVar.empty[String]
          // Would block if full
          _ <- tmvar.put("Hello")
          // Would block if empty
          hello <- tmvar.take
          _ <- tmvar.put("world")
          world <- tmvar.read
        yield hello |+| world
      }
      _ <- IO.println(result)
    yield ()
