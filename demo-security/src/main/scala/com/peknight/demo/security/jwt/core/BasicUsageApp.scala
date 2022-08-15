package com.peknight.demo.security.jwt.core

import cats.effect.{IO, IOApp}
import pdi.jwt.*
import cats.syntax.traverse.*

import scala.util.Try
import fs2.Stream
import fs2.text.utf8
import fs2.text.base64
import scodec.bits.Bases.Alphabets.Base64Url

object BasicUsageApp extends IOApp.Simple:

  val key: String = "secretKey"

  val run: IO[Unit] =
    for
      token <- IO(Jwt.encode("""{"user":1}""", key, JwtAlgorithm.HS256))
      _ <- IO.println(s"token: $token")
      decodeResult <- IO(Jwt.decodeRawAll(token, key, Seq(JwtAlgorithm.HS256)))
      _ <- IO.println(s"decodeResult: $decodeResult")
      wrongResult <- IO(Jwt.decodeRawAll(token, "wrongKey", Seq(JwtAlgorithm.HS256)))
      _ <- IO.println(s"wrongResult: $wrongResult")
    yield ()
