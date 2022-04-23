package com.peknight.demo.fs2

import cats.effect.{IO, IOApp}
import com.peknight.common.core.std.Random
import fs2.Stream

object PullApp extends IOApp.Simple:

  case class Point(x: Double, y: Double)

  case class Runtime(random: Random, point: Point)

  val run =
    for
      _ <- IO.println("Hello, world!")
    yield ()


