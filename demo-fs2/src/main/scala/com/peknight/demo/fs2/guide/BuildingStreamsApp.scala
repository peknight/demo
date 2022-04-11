package com.peknight.demo.fs2.guide

import cats.effect.{IO, IOApp}
import fs2.Stream

object BuildingStreamsApp extends IOApp.Simple {
  val s0 = Stream.empty
  val s1 = Stream.emit(1)
  val s1a = Stream(1, 2, 3)
  val s1b = Stream.emits(List(1, 2, 3))

  val eff = Stream.eval(IO { println("BEING RUN!!"); 1 + 1 })

  val run = for {
    _ <- IO.println(s1.toList)
    _ <- IO.println(s1.toVector)
    _ <- IO.println((Stream(1, 2, 3) ++ Stream(4, 5)).toList)
    _ <- IO.println(Stream(1, 2, 3).map(_ + 1).toList)
    _ <- IO.println(Stream(1, 2, 3).filter(_ % 2 != 0).toList)
    _ <- IO.println(Stream(1, 2, 3).fold(0)(_ + _).toList)
    _ <- IO.println(Stream(None, Some(2), Some(3)).collect { case Some(i) => i }.toList)
    _ <- IO.println(Stream.range(0, 5).intersperse(42).toList)
    _ <- IO.println(Stream(1, 2, 3).flatMap(i => Stream(i, i)).toList)
    _ <- IO.println(Stream(1, 2, 3).repeat.take(9).toList)
    _ <- IO.println(Stream(1, 2, 3).repeatN(2).toList)
  } yield ()
}
