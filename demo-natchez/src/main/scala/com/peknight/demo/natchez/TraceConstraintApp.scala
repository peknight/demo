package com.peknight.demo.natchez

import cats.Monad
import cats.data.Kleisli
import cats.effect.{IO, IOApp, MonadCancel}
import cats.mtl.Local
import com.peknight.demo.natchez.SpansApp.wibble
import natchez.Span
import natchez.mtl.given

object TraceConstraintApp extends IOApp.Simple:
  // No-Op Instance
  object NoTraceForYou:
    // In this scope we will ignore tracing
    import natchez.Trace.Implicits.noop

    // So this compiles fine
    def go[F[_]: Monad]: F[Unit] = wibble("bob", 42)
  end NoTraceForYou

  // Kleisli Instance
  def go[F[_]](span: Span[F])(using ev: MonadCancel[F, Throwable]): F[Unit] =
    wibble[[X] =>> Kleisli[F, Span[F], X]]("bob", 42).run(span)

  def goLocal[F[_]](using ev0: MonadCancel[F, Throwable], ev1: Local[F, Span[F]]): F[Unit] =
    wibble("bob", 42)

  val run: IO[Unit] =
    for
      _ <- IO.unit
    yield
      ()
end TraceConstraintApp
