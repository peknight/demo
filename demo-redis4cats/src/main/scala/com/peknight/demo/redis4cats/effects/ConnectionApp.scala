package com.peknight.demo.redis4cats.effects

import cats.effect.{IO, IOApp, Resource}
import com.peknight.demo.redis4cats.{commandsApi, redisUri}
import dev.profunktor.redis4cats.{Redis, RedisCommands}
import dev.profunktor.redis4cats.connection.RedisClient
import dev.profunktor.redis4cats.data.RedisCodec
import dev.profunktor.redis4cats.effect.Log.NoOp.*

object ConnectionApp extends IOApp.Simple:

  def putStrLn(str: String): IO[Unit] = IO.println(str)

  val run = commandsApi.use { redis => redis.ping.flatMap(putStrLn) }
