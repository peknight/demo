package com.peknight.demo.fs2.guide

import cats.effect.{IO, IOApp}
import fs2.Stream

object ExercisesStreamBuildingApp extends IOApp.Simple:
  val run =
    for
      _ <- IO.println(Stream(1, 0).repeat.take(6).toList)
      _ <- IO.println(Stream(1, 2, 3).drain.toList)
      res <- Stream.exec(IO.println("!!")).compile.toVector
      _ <- IO.println(res)
      _ <- IO.println((Stream(1, 2) ++ Stream(3).map(_ => throw new Exception("nooo!!!"))).attempt.toList)
    yield ()
