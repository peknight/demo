package com.peknight.demo.redis4cats.quickstart

import cats.effect.{IO, IOApp}
import cats.syntax.eq.*
import com.peknight.demo.redis4cats.redisUri
import dev.profunktor.redis4cats.Redis
import dev.profunktor.redis4cats.effect.Log.Stdout.*

object QuickStartApp extends IOApp.Simple:

  def run =
    Redis[IO].utf8(redisUri).use { redis =>
      for
        _ <- redis.set("foo", "123")
        x <- redis.get("foo")
        _ <- redis.setNx("foo", "should not happen")
        y <- redis.get("foo")
        _ <- IO.println(x === y)
      yield ()
    }

