package com.peknight.demo.fs2.guide

import cats.effect.{IO, IOApp, Sync}
import fs2.Stream

object SynchronousEffectsApp extends IOApp.Simple:

  def destroyUniverse(): Unit = println("BOOOOM!!!")

  val s = Stream.exec(IO(destroyUniverse())) ++ Stream("...moving on")

  val T = Sync[IO]

  val s2 = Stream.exec(T.delay(destroyUniverse())) ++ Stream("...moving on")

  val run =
    for
      _ <- s.compile.toVector.flatMap(IO.println)
      _ <- s2.compile.toVector.flatMap(IO.println)
    yield ()
