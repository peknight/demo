package com.peknight.demo.redis4cats.effects

import cats.effect.{IO, IOApp}
import com.peknight.demo.redis4cats.commandsApi
import dev.profunktor.redis4cats.effects.ScriptOutputType

object ScriptingApp extends IOApp.Simple:

  def putStrLn(str: String): IO[Unit] = IO.println(str)

  val run = commandsApi.use { redis =>
    for
      greeting <- redis.eval("return 'Hello World'", ScriptOutputType.Value)
      _ <- putStrLn(s"Greetings from Lua: $greeting")
    yield ()
  }
