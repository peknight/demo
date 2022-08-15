package com.peknight.demo.security.jwt.core

import cats.effect.{IO, IOApp}
import pdi.jwt.*

import java.time.Clock

object OptionsApp extends IOApp.Simple:

  given Clock = Clock.systemUTC()

  val run: IO[Unit] =
    for
      expiredToken <- IO(Jwt.encode(JwtClaim().by("me").expiresIn(-1)))
      _ <- IO.println(s"expiredToken: $expiredToken")
      // Fail since the token is expired
      expiredIsValid <- IO(Jwt.isValid(expiredToken))
      _ <- IO.println(s"expiredIsValid: $expiredIsValid")
      expiredDecode <- IO(Jwt.decode(expiredToken))
      _ <- IO.println(s"expiredDecode: $expiredDecode")
      // Let's disable expiration check
      disableExpirationIsValid <- IO(Jwt.isValid(expiredToken, JwtOptions(expiration = false)))
      _ <- IO.println(s"disableExpirationIsValid: $disableExpirationIsValid")
      disableExpirationDecode <- IO(Jwt.decode(expiredToken, JwtOptions(expiration = false)))
      _ <- IO.println(s"disableExpirationDecode: $disableExpirationDecode")
      // Allow 30sec leeway
      leewayIsValid <- IO(Jwt.isValid(expiredToken, JwtOptions(leeway = 30)))
      _ <- IO.println(s"leewayIsValid: $leewayIsValid")
      leewayDecode <- IO(Jwt.decode(expiredToken, JwtOptions(leeway = 30)))
      _ <- IO.println(s"leewayDecode: $leewayDecode")
    yield ()
