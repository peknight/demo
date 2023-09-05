package com.peknight.demo.catseffect.standard

import cats.effect.{IO, IOApp}
import cats.effect.cps.*

import scala.concurrent.duration.*

object AsyncAwaitApp extends IOApp.Simple:
  val io = IO.sleep(50.millis).as(1)
  val program: IO[Int] = async[IO] { io.await + io.await }
  // equivalent to
  val programViaFor: IO[Int] =
    for
      x1 <- io
      x2 <- io
    yield (x1 + x2)

  // Known limitations

  /*
   * 文档说好会报错，但是没有报错，还运行良好
   * `await` cannot be called from within local methods or lambdas
   * (which prevents its use in `for` loops (that get translated to a `foreach` call).
   */

  val programCompileError: IO[Int] = async[IO] {
    var n = 0
    for i <- 1 to 3 do
      n += IO.pure(i).await
    n
  }

  val programViaWhile: IO[Int] = async[IO] {
    var n = 0
    var i = 1
    while i <= 3 do
      n += IO.pure(i).await
      i += 1
    n
  }

  val run: IO[Unit] =
    for
      res <- program
      _ <- IO.println(res)
    yield ()


