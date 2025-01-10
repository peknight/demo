package com.peknight.demo.cats.effect.unique

import cats.Monad
import cats.effect.{IO, IOApp, Unique}
import cats.syntax.apply.*
import cats.syntax.eq.*
import cats.syntax.all.*

object UniqueApp extends IOApp.Simple:

  def token[F[_] : Unique]: F[Unique.Token] = Unique[F].unique

  def tokenCheck[F[_]: Monad: Unique] =
    (token[F], token[F]).mapN { (x, y) => x === y }

  val run: IO[Unit] =
    for
      res <- tokenCheck[IO]
      _ <- IO.println(res)
    yield ()
