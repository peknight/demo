package com.peknight.demo.security.jwt.core

import cats.effect.{IO, IOApp}
import pdi.jwt.*

import java.time.Clock

object EncodingApp extends IOApp.Simple:

  val run: IO[Unit] =
    for
      // Encode from string, header automatically generated
      token1 <- IO(Jwt.encode("""{"user":1}""", "secretKey", JwtAlgorithm.HS384))
      _ <- IO.println(s"token1: $token1")
      // Encode from case class, header automatically generated
      // Set that the token has been issued now and expires in 10 seconds
      given Clock = Clock.systemUTC()
      token2 <- IO(Jwt.encode(JwtClaim("""{"user":1}""").issuedNow.expiresIn(10), "secretKey",
        JwtAlgorithm.HS512))
      _ <- IO.println(s"token2: $token2")
      // You can encode without signing it
      token3 <- IO(Jwt.encode("""{"user":1}"""))
      _ <- IO.println(s"token3: $token3")
      // You can specify a string header but also need to specify the algorithm just to be sure
      // This is not really typesafe, so please use it with care
      token4 <- IO(Jwt.encode("""{"typ":"JWT","alg":"HS256"}""", """{"user":1}""", "key",
        JwtAlgorithm.HS256))
      _ <- IO.println(s"token4: $token4")
      // If using a case class header, no need to repeat the algorithm
      // This is way better than the previous one
      token5 <- IO(Jwt.encode(JwtHeader(JwtAlgorithm.HS256), JwtClaim("""{"user":1}"""), "key"))
      _ <- IO.println(s"token5: $token5")
    yield ()
