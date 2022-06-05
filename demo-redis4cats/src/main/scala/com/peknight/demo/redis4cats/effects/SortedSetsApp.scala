package com.peknight.demo.redis4cats.effects

import cats.effect.{IO, IOApp, Resource}
import com.peknight.demo.redis4cats.codecs.CodecsApp
import com.peknight.demo.redis4cats.redisUri
import dev.profunktor.redis4cats.algebra.SortedSetCommands
import dev.profunktor.redis4cats.connection.RedisClient
import dev.profunktor.redis4cats.data.RedisCodec
import dev.profunktor.redis4cats.effect.Log.NoOp.*
import dev.profunktor.redis4cats.effects.{Score, ScoreWithValue, ZRange}
import dev.profunktor.redis4cats.{Redis, RedisCommands}

object SortedSetsApp extends IOApp.Simple:

  val testKey = "zztop"

  def putStrLn(str: String): IO[Unit] = IO.println(str)

  val commandsApi: Resource[IO, RedisCommands[IO, String, Long]] =
    RedisClient[IO].from(redisUri).flatMap(Redis[IO].fromClient(_, CodecsApp.longCodec))

  val run = commandsApi.use { redis =>
    for
      _ <- redis.zAdd(testKey, args = None, ScoreWithValue(Score(1), 1), ScoreWithValue(Score(3), 2))
      x <- redis.zRevRangeByScore(testKey, ZRange(0, 2), limit = None)
      _ <- putStrLn(s"Score: $x")
      y <- redis.zCard(testKey)
      _ <- putStrLn(s"Size: $y")
      z <- redis.zCount(testKey, ZRange(0, 1))
      _ <- putStrLn(s"Count: $z")
    yield ()
  }
