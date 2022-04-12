package com.peknight.demo.fs2.guide

import cats.effect.{IO, IOApp}
import fs2.Stream

object ExercisesStreamTransformingApp extends IOApp.Simple {

  val run = for {
    _ <- IO.println(Stream.range(0, 100).takeWhile(_ < 7).toList)
    _ <- IO.println(Stream ("Alice", "Bob", "Carol").intersperse("|").toList)
    _ <- IO.println(Stream.range(1, 10).scan(0) (_ + _).toList)
  } yield ()
}
