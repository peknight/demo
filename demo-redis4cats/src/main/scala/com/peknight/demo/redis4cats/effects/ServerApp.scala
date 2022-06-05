package com.peknight.demo.redis4cats.effects

import cats.effect.{IO, IOApp}
import com.peknight.demo.redis4cats.commandsApi

object ServerApp extends IOApp.Simple:

  def putStrLn(str: String): IO[Unit] = IO.println(str)

  val run = commandsApi.use { redis => redis.flushAll }


