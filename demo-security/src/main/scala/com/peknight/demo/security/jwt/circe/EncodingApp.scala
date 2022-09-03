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
      Right(claimJson) = claimJsonEither: @unchecked
      _ <- IO.println(s"claimJson: $claimJson")
      headerEither <- IO(jawnParse("""{"typ":"JWT","alg":"HS256"}"""))
      Right(header) = headerEither: @unchecked
      _ <- IO.println(s"header: $header")
      noKeyEncode <- IO(JwtCirce.encode(claimJson))
      _ <- IO.println(s"noKeyEncode: $noKeyEncode")
      encode <- IO(JwtCirce.encode(claimJson, key, algo))
      _ <- IO.println(s"encode: $encode")
      headerEncode <- IO(JwtCirce.encode(header, claimJson, key))
      _ <- IO.println(s"headerEncode: $headerEncode")
    yield ()
