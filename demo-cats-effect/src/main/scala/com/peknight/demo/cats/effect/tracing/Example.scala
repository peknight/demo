package com.peknight.demo.cats.effect.tracing

import cats.effect.{IO, IOApp}

import scala.util.Random

/**
 * Pass the following system property to your JVM:
 * -Dcats.effect.tracing.mode=full
 * -Dcats.effect.tracing.buffer.size=64
 */
object Example extends IOApp.Simple:

  def fib(n: Int, a: Long = 0, b: Long = 1): IO[Long] =
    IO(a + b).flatMap { b2 =>
      if n > 0 then fib(n - 1, b, b2)
      else IO.pure(b2)
    }

  def program: IO[Unit] =
    for
      x <- fib(20)
      _ <- IO(println(s"The 20th fibonacci number is $x"))
      _ <- IO(Random.nextBoolean())
        .ifM(IO.raiseError(new Throwable("Boom!")), IO.unit)
    yield ()

  override def run: IO[Unit] =
    program
