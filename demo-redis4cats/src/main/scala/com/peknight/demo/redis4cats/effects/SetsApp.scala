package com.peknight.demo.redis4cats.effects

import cats.effect.{IO, IOApp}
import com.peknight.demo.redis4cats.commandsApi

object SetsApp extends IOApp.Simple:
  val testKey = "foos"

  def putStrLn(str: String): IO[Unit] = IO.println(str)

  val showResult: Set[String] => IO[Unit] = x => putStrLn(s"$testKey members: $x")

  val run = commandsApi.use { redis =>
    for
      x <- redis.sMembers(testKey)
      _ <- showResult(x)
      _ <- redis.sAdd(testKey, "set value")
      y <- redis.sMembers(testKey)
      _ <- showResult(y)
      _ <- redis.sCard(testKey).flatMap(s => putStrLn(s"size: ${s.toString}"))
      _ <- redis.sRem("non-existing", "random")
      w <- redis.sMembers(testKey)
      _ <- showResult(w)
      _ <- redis.sRem(testKey, "set value")
      z <- redis.sMembers(testKey)
      _ <- showResult(z)
      _ <- redis.sCard(testKey).flatMap(s => putStrLn(s"size: ${s.toString}"))
    yield ()
  }

