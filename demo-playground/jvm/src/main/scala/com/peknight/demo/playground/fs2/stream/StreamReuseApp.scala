package com.peknight.demo.playground.fs2.stream

import cats.effect.{IO, IOApp}
import fs2.Stream

object StreamReuseApp extends IOApp.Simple:
  val stream = Stream.unfoldEval(1) { s =>
    IO.println(s).as(Some(s, s + 1))
  }.take(5)
  val stream1 = stream.map(a => s"$a!")
  val stream2 = stream.map(a => s"$a?")
  val run =
    for
      s0 <- stream.compile.toVector
      _ <- IO.println(s0)
      s1 <- stream1.compile.toVector
      _ <- IO.println(s1)
      s2 <- stream2.compile.toVector
      _ <- IO.println(s2)
    yield ()

