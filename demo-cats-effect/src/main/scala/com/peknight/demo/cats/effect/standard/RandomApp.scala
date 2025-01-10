package com.peknight.demo.cats.effect.standard

import cats.Functor
import cats.effect.std.Random
import cats.effect.{IO, IOApp}
import cats.implicits.toFunctorOps

object RandomApp extends IOApp.Simple:

  def dieRoll[F[_]: Functor: Random]: F[Int] = Random[F].betweenInt(0, 6).map(_ + 1)

  val run =
    for
      random <- Random.scalaUtilRandom[IO]
      die <- dieRoll[IO](using summon[Functor[IO]], random)
      _ <- IO.println(die)
    yield ()
