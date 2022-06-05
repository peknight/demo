package com.peknight.demo.redis4cats.effects

import cats.effect.{IO, IOApp}
import com.peknight.demo.redis4cats.commandsApi
import scala.concurrent.duration.*

object KeysApp extends IOApp.Simple:

  val key = "users"

  val run = commandsApi.use { redis =>
    for
      _ <- redis.del(key)
      _ <- redis.exists(key)
      _ <- redis.expire(key, 5.seconds)
    yield ()
  }
