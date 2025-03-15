package com.peknight.demo.cats.effect.iolocal

import cats.Monad
import cats.effect.std.Console
import cats.effect.{IO, IOApp, IOLocal, Sync}
import cats.syntax.flatMap.*
import cats.syntax.functor.*

import scala.concurrent.duration.*

object IOLocalApp extends IOApp.Simple:
  def inc(idx: Int, local: IOLocal[Int]): IO[Unit] =
    local.update(_ + 1) >> local.get.flatMap(current => IO.println(s"update $idx: $current"))

  def update(name: String, local: IOLocal[Int], f: Int => Int): IO[Unit] =
    local.update(f) >> local.get.flatMap(current => IO.println(s"$name: $current"))

  def service[F[_]: {Sync, Console, TraceIdScope}]: F[String] =
    for
      traceId <- TraceId.gen[F]
      result <- TraceIdScope[F].scope(traceId).use(_ => callRemote[F])
    yield result

  def callRemote[F[_]: {Monad, Console, TraceIdScope}]: F[String] =
    for
      traceId <- TraceIdScope[F].get
      result <- Console[F].println(s"Processing request. TraceId: $traceId")
    yield "some response"

  val run: IO[Unit] =
    for
      local <- IOLocal(42)
      _ <- inc(1, local)
      _ <- inc(2, local)
      current <- local.get
      _ <- IO.println(s"fiber A: $current")
      fiberA <- update("fiber B", local, _ - 1).start
      fiberB <- update("fiber C", local, _ + 1).start
      _ <- fiberA.joinWithNever
      _ <- fiberB.joinWithNever
      current <- local.get
      _ <- IO.println(s"fiber A: $current")
      traceIdScope <- TraceIdScope.fromIOLocal
      given TraceIdScope[IO] = traceIdScope
      _ <- service[IO]
    yield ()
