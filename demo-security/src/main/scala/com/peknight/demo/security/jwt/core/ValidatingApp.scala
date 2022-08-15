package com.peknight.demo.security.jwt.core

import cats.effect.{IO, IOApp}
import pdi.jwt.*

import java.time.Clock
import scala.util.Try

object ValidatingApp extends IOApp.Simple:

  val run: IO[Unit] =
    for
      token <- IO(Jwt.encode("""{"user":1}""", "secretKey", JwtAlgorithm.HS256))
      // All good
      allGoodValidate <- IO(Jwt.validate(token, "secretKey", Seq(JwtAlgorithm.HS256)))
      _ <- IO.println(s"allGoodValidate: $allGoodValidate")
      allGoodIsValid <- IO(Jwt.isValid(token, "secretKey", Seq(JwtAlgorithm.HS256)))
      _ <- IO.println(s"allGoodIsValid: $allGoodIsValid")
      // Wrong key here
      wrongKeyValidate <- IO(Try(Jwt.validate(token, "wrongKey", Seq(JwtAlgorithm.HS256))))
      _ <- IO.println(s"wrongKeyValidate: $wrongKeyValidate")
      wrongKeyIsValid <- IO(Jwt.isValid(token, "wrongKey", Seq(JwtAlgorithm.HS256)))
      _ <- IO.println(s"wrongKeyIsValid: $wrongKeyIsValid")
      // No key for unsigned token => ok
      noKeyValidate <- IO(Jwt.validate(Jwt.encode("{}")))
      _ <- IO.println(s"noKeyValidate: $noKeyValidate")
      noKeyIsValid <- IO(Jwt.isValid(Jwt.encode("{}")))
      _ <- IO.println(s"noKeyIsValid: $noKeyIsValid")
      // No key while the token is actually signed => wrong
      keyValidate <- IO(Try(Jwt.validate(token)))
      _ <- IO.println(s"keyValidate: $keyValidate")
      keyIsValid <- IO(Jwt.isValid(token))
      _ <- IO.println(s"keyIsValid: $keyIsValid")
      // The token hasn't started yet!
      given Clock = Clock.systemUTC()
      notStartedValidate <- IO(Try(Jwt.validate(Jwt.encode(JwtClaim().startsIn(5)))))
      _ <- IO.println(s"notStartedValidate: $notStartedValidate")
      notStartedIsValid <- IO(Jwt.isValid(Jwt.encode(JwtClaim().startsIn(5))))
      _ <- IO.println(s"notStartedIsValid: $notStartedIsValid")
      // This is no token
      noTokenValidate <- IO(Try(Jwt.validate("a.b.c")))
      _ <- IO.println(s"noTokenValidate: $noTokenValidate")
      noTokenIsValid <- IO(Jwt.isValid("a.b.c"))
      _ <- IO.println(s"noTokenIsValid: $noTokenIsValid")
    yield ()
