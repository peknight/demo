package com.peknight.demo.natchez

import cats.Monad
import cats.effect.{IO, IOApp}
import cats.syntax.functor.*
import natchez.{Span, Trace}

object SpansApp extends IOApp.Simple:
  def wibbleIO(name: String, age: Int, parent: Span[IO]): IO[Unit] =
    parent.span("wibble").use { span =>
      for
        _ <- span.put("name" -> name, "age" -> age)
        // ... actual method logic in IO
      yield
        ()
    }
  def wibble[F[_]: {Monad, Trace}](name: String, age: Int): F[Unit] =
    Trace[F].span("wibble") {
      for
        _ <- Trace[F].put("name" -> name, "age" -> age)
      // ... actual method logic in F
      yield
        ()
    }
  val run: IO[Unit] =
    for
      _ <- IO.unit
    yield
      ()
end SpansApp
