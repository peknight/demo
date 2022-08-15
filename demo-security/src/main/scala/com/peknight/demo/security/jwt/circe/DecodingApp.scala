package com.peknight.demo.security.jwt.circe

import java.time.Instant
import pdi.jwt.*
import cats.effect.{IO, IOApp}

object DecodingApp extends IOApp.Simple:
  val run: IO[Unit] =
    for
      claim <- IO(JwtClaim(
        expiration = Some(Instant.now.plusSeconds(157784760).getEpochSecond),
        issuedAt = Some(Instant.now.getEpochSecond)
      ))
      _ <- IO.println(s"claim: $claim")
      key = "secretKey"
      algo = JwtAlgorithm.HS256
      token <- IO(JwtCirce.encode(claim, key, algo))
      _ <- IO.println(s"token: $token")
      // You can decode to JsObject
      decodeJson <- IO(JwtCirce.decodeJson(token, key, Seq(JwtAlgorithm.HS256)))
      _ <- IO.println(s"decodeJson: $decodeJson")
      decodeJsonAll <- IO(JwtCirce.decodeJsonAll(token, key, Seq(JwtAlgorithm.HS256)))
      _ <- IO.println(s"decodeJsonAll: $decodeJsonAll")
      // Or to case classes
      decode <- IO(JwtCirce.decode(token, key, Seq(JwtAlgorithm.HS256)))
      _ <- IO.println(s"decode: $decode")
      decodeAll <- IO(JwtCirce.decodeAll(token, key, Seq(JwtAlgorithm.HS256)))
      _ <- IO.println(s"decodeAll: $decodeAll")
    yield ()
