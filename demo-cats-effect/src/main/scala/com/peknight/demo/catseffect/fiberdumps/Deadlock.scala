package com.peknight.demo.catseffect.fiberdumps

import cats.effect.{IO, IOApp}

/**
 * 可以进入sbt 运行这个程序，然后按ctrl-T 即可把fiber堆栈dump出来
 * 也可以按ctrl-\ 把JVM线程堆栈dump出来
 */
object Deadlock extends IOApp.Simple:
  val run =
    for
      latch <- IO.deferred[Unit]
      body = latch.get
      fiber <- body.start
      _ <- fiber.join
      _ <- latch.complete(())
    yield ()
