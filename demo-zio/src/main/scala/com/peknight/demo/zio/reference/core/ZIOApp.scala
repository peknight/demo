package com.peknight.demo.zio.reference.core

import zio.*

object ZIOApp extends ZIOAppDefault:

  val s1 = ZIO.succeed(42)
  val s2: Task[Int] = ZIO.succeed(42)

  def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] =
    for
      s1Res <- s1
      _ <- Console.printLine(s1Res)
      s2Res <- s2
      _ <- Console.printLine(s2Res)
    yield ()
end ZIOApp
