package com.peknight.demo.security.jwt.core

import cats.effect.{IO, IOApp}
import pdi.jwt.*

import java.time.{Clock, Instant, ZoneId}

object CustomClockApp extends IOApp.Simple:


  val run: IO[Unit] =
    for
      startTime <- IO(Clock.fixed(Instant.ofEpochSecond(0), ZoneId.of("UTC")))
      _ <- IO.println(s"startTime: $startTime")
      endTime <- IO(Clock.fixed(Instant.ofEpochSecond(5), ZoneId.of("UTC")))
      _ <- IO.println(s"endTime: $endTime")
      customJwt <- IO(Jwt(endTime))
      _ <- IO.println(s"customJwt: $customJwt")
      claim <- IO(JwtClaim().issuedNow(using startTime).expiresIn(10)(using startTime))
      _ <- IO.println(s"claim: $claim")
      encoded <- IO(customJwt.encode(claim, "key", JwtAlgorithm.HS256))
      _ <- IO.println(s"encoded: $encoded")
      res <- IO(customJwt.decode(encoded, "key", JwtAlgorithm.allHmac()))
      _ <- IO.println(s"res: $res")
    yield ()