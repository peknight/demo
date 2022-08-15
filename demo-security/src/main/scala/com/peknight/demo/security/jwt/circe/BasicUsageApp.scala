package com.peknight.demo.security.jwt.circe

import cats.effect.{IO, IOApp}
import pdi.jwt.*

import java.time.Instant

object BasicUsageApp extends IOApp.Simple:

  val run: IO[Unit] =
    for
      claim <- IO(JwtClaim(
        expiration = Some(Instant.now.plusSeconds(157784760).getEpochSecond),
        issuedAt = Some(Instant.now.getEpochSecond)
      ))
      key = "secretKey"
      algo = JwtAlgorithm.HS256
      token <- IO(JwtCirce.encode(claim, key, algo))
      decodeJson <- IO(JwtCirce.decodeJson(token, key, Seq(JwtAlgorithm.HS256)))
      _ <- IO.println(s"decodeJson: $decodeJson")
      decode <- IO(JwtCirce.decode(token, key, Seq(JwtAlgorithm.HS256)))
      _ <- IO.println(s"decode: $decode")
    yield ()
