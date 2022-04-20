package com.peknight.demo.catseffect.spawn

import cats.effect.kernel.MonadCancel
import cats.effect.syntax.all._
import cats.effect.{IO, IOApp, Spawn}
import cats.syntax.all._

import scala.concurrent.duration.DurationInt

object SpawnApp extends IOApp.Simple:
  def endpoint[F[_]: Spawn](server: Server[F])(body: Array[Byte] => F[Array[Byte]]): F[Unit] =
    def handle(conn: Connection[F]): F[Unit] =
      for
        request <- conn.read
        response <- body(request)
        _ <- conn.write(response)
      yield ()

    val handler = MonadCancel[F].uncancelable { poll =>
      poll(server.accept).flatMap { conn =>
        handle(conn).guarantee(conn.close).start
      }
    }
    handler.foreverM

  val cancelation =
    for
      target <- IO.println("Catch me if you can!").foreverM.start
      _ <- IO.sleep(1.second)
      _ <- target.cancel
    yield ()

  // parTraverse任意一个失败，其余均会被取消
  val parTraverseList = (-10 to 10).toList.parTraverse(i => IO(5f / i))

  def both[F[_]: Spawn, A, B](fa: F[A], fb: F[B]): F[(A, B)] =
    for
      fiberA <- fa.start
      fiberB <- fb.start
      a <- fiberA.joinWithNever
      b <- fiberB.joinWithNever
    yield (a, b)

  val run =
    for
      _ <- cancelation
      list <- parTraverseList
      _ <- IO.println(list)
    yield ()
