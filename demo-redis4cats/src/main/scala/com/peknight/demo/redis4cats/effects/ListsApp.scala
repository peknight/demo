package com.peknight.demo.redis4cats.effects

import cats.effect.{IO, IOApp}
import com.peknight.demo.redis4cats.commandsApi

object ListsApp extends IOApp.Simple:

  val testKey = "listos"

  def putStrLn(str: String): IO[Unit] = IO.println(str)

  val run = commandsApi.use { redis =>
    for
      _ <- redis.rPush(testKey, "one", "two", "three")
      x <- redis.lRange(testKey, 0, 10)
      _ <- putStrLn(s"Range: $x")
      y <- redis.lLen(testKey)
      _ <- putStrLn(s"Length: $y")
      a <- redis.lPop(testKey)
      _ <- putStrLn(s"Left Pop: $a")
      b <- redis.rPop(testKey)
      _ <- putStrLn(s"Right Pop: $b")
      z <- redis.lRange(testKey, 0, 10)
      _ <- putStrLn(s"Range: $z")
    yield ()
  }
