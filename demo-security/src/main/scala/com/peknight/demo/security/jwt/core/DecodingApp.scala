package com.peknight.demo.security.jwt.core

import cats.effect.{IO, IOApp}
import pdi.jwt.*

import java.time.Clock

object DecodingApp extends IOApp.Simple:

  val run: IO[Unit] =
    for
      token <- IO(Jwt.encode("""{"user":1}""", "secretKey", JwtAlgorithm.HS256))
      // Decode all parts of the token as string
      rawAll <- IO(Jwt.decodeRawAll(token, "secretKey", JwtAlgorithm.allHmac()))
      _ <- IO.println(s"rawAll: $rawAll")
      // Decode only the claim as a string
      rawClaim <- IO(Jwt.decodeRaw(token, "secretKey", Seq(JwtAlgorithm.HS256)))
      _ <- IO.println(s"rawClaim: $rawClaim")
      // Decode all parts and cast them as a better type if possible.
      // Since the implementation in JWT Core only use string, it is the same as decodeRawAll
      // But check the result in JWT Play JSON to see the difference
      all <- IO(Jwt.decodeAll(token, "secretKey", Seq(JwtAlgorithm.HS256)))
      _ <- IO.println(s"all: $all")
      // Same as before, but only the claim
      // (you should start to see a pattern in the naming convention of the functions)
      claim <- IO(Jwt.decode(token, "secretKey", Seq(JwtAlgorithm.HS256)))
      _ <- IO.println(s"claim: $claim")
      // Failure because the token is not a token at all
      notTokenFailure <- IO(Jwt.decode("Hey there!"))
      _ <- IO.println(s"notTokenFailure: $notTokenFailure")
      // Failure if not Base64 encoded
      notBase64Failure <- IO(Jwt.decode("a.b.c"))
      _ <- IO.println(s"notBase64Failure: $notBase64Failure")
      // Failure in case we use the wrong key
      wrongKeyFailure <- IO(Jwt.decode(token, "wrongKey", Seq(JwtAlgorithm.HS256)))
      _ <- IO.println(s"wrongKeyFailure: $wrongKeyFailure")
      // Failure if the token only starts in seconds
      given Clock = Clock.systemUTC()
      startsFailure <- IO(Jwt.decode(Jwt.encode(JwtClaim().startsIn(5))))
      _ <- IO.println(s"startsFailure: $startsFailure")
    yield ()

