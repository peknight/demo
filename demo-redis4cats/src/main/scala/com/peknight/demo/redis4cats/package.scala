package com.peknight.demo

import cats.effect.{IO, Resource}
import dev.profunktor.redis4cats.connection.RedisClient
import dev.profunktor.redis4cats.data.RedisCodec
import dev.profunktor.redis4cats.effect.Log.NoOp.*
import dev.profunktor.redis4cats.{Redis, RedisCommands}

package object redis4cats:
  
  val redisUri = "redis://redis4cats@localhost"
  
  val commandsApi: Resource[IO, RedisCommands[IO, String, String]] =
    RedisClient[IO].from(redisUri).flatMap(Redis[IO].fromClient(_, RedisCodec.Utf8))

