package com.peknight.demo.fs2.guide

import cats.effect.{IO, IOApp}
import fs2.Stream

object BasicStreamOperationsApp extends IOApp.Simple {

  val appendEx1 = Stream(1, 2, 3) ++ Stream.emit(42)

  val appendEx2 = Stream(1, 2, 3) ++ Stream.eval(IO.pure(4))

  val run = for {
    _ <- IO.println(appendEx1.toVector)
    ex2 <- appendEx2.compile.toVector
    _ <- IO.println(ex2)
    _ <- IO.println(appendEx1.map(_ + 1).toList)
    _ <- IO.println(appendEx1.flatMap(i => Stream.emits(List(i, i))).toList)
  } yield ()
}
