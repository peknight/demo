package com.peknight.demo.security.jwt.circe

import cats.effect.{IO, IOApp}
import io.circe.*
import io.circe.jawn.parse as jawnParse
import pdi.jwt.*
import java.time.Instant

object EncodingApp extends IOApp.Simple:

  val key = "secretKey"
  val algo = JwtAlgorithm.HS256

  val run: IO[Unit] =
    for
      claimJsonEither <- IO(jawnParse(s"""{"expires":${Instant.now.getEpochSecond}}"""))
      _ <- IO.println(s"claimJsonEither: $claimJsonEither")
      headerEither <- IO(jawnParse("""{"typ":"JWT","alg":"HS256"}"""))
      _ <- IO.println(s"headerEither: $headerEither")
    yield ()
