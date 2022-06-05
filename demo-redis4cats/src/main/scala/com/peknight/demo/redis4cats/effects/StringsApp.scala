package com.peknight.demo.redis4cats.effects

import cats.effect.{IO, IOApp}
import com.peknight.demo.redis4cats.commandsApi

object StringsApp extends IOApp.Simple:

  val usernameKey = "users"

  def putStrLn(str: String): IO[Unit] = IO.println(str)

  val showResult: Option[String] => IO[Unit] = _.fold(putStrLn(s"Not found key: $usernameKey"))(s => putStrLn(s))

  val run = commandsApi.use { redis =>
    for
      x <- redis.get(usernameKey)
      _ <- showResult(x)
      _ <- redis.set(usernameKey, "gvolpe")
      y <- redis.get(usernameKey)
      _ <- showResult(y)
      _ <- redis.setNx(usernameKey, "should not happen")
      w <- redis.get(usernameKey)
      _ <- showResult(w)
    yield ()
  }
