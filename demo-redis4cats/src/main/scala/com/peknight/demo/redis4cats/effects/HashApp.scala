package com.peknight.demo.redis4cats.effects

import cats.effect.{IO, IOApp}
import com.peknight.demo.redis4cats.commandsApi

object HashApp extends IOApp.Simple:

  val testKey = "foo_hash"
  val testField = "bar"

  def putStrLn(str: String): IO[Unit] = IO.println(str)

  val showResult: Option[String] => IO[Unit] =
    _.fold(putStrLn(s"Not found key: $testKey | field: $testField"))(putStrLn)

  val run = commandsApi.use { redis =>
    for
      x <- redis.hGet(testKey, testField)
      _ <- showResult(x)
      _ <- redis.hSet(testKey, testField, "some value")
      y <- redis.hGet(testKey, testField)
      _ <- showResult(y)
      _ <- redis.hSetNx(testKey, testField, "should not happen")
      w <- redis.hGet(testKey, testField)
      _ <- showResult(w)
      _ <- redis.hDel(testKey, testField)
      z <- redis.hGet(testKey, testField)
      _ <- showResult(z)
    yield ()
  }
