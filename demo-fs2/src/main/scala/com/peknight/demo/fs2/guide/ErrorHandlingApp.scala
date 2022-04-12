package com.peknight.demo.fs2.guide

import cats.effect.{IO, IOApp}
import fs2.Stream

/**
 * Don't use `handleErrorWith` for doing resource cleanup; use `bracket`.
 */
object ErrorHandlingApp extends IOApp.Simple {
  val err = Stream.raiseError[IO](new Exception("oh noes!"))

  val err2 = Stream(1, 2, 3) ++ (throw new Exception("!@#$"))

  val err3 = Stream.eval(IO(throw new Exception("error in effect!!!")))

  val err4 = Stream(1, 2, 3).covary[IO] ++ Stream.raiseError[IO](new Exception("bad things!")) ++ Stream.eval(IO(4))

  val run = for {
    _ <- err.compile.toList.handleErrorWith(IO.println(_))
    _ <- IO(err2.toList).flatMap(IO.println).handleErrorWith(IO.println)
    _ <- err3.compile.drain.handleErrorWith(IO.println)
    _ <- err.handleErrorWith { e => Stream.emit(e.getMessage) }.compile.toList.flatMap(IO.println)
    /*
     * even when using `handleErrorWith` (or `attempt`) the stream will be terminated after the error
     * and no more values will be pulled.
     */
    _ <- err4.handleErrorWith { _ => Stream(0) }.compile.toList.flatMap(IO.println)
  } yield ()
}
