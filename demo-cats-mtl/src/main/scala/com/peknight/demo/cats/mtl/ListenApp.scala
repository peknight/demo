package com.peknight.demo.cats.mtl

import cats.Monad
import cats.mtl.syntax.listen.*
import cats.syntax.all.*
import cats.data.{Chain, Writer}
import cats.mtl.{Listen, Tell}

object ListenApp extends App:
  def sendToServer[F[_]: Monad](logs: Chain[String]): F[Unit] =
    // impure implementation for demonstrative purposes, please don't do this at home
    Monad[F].pure(println(show"Sending to server: $logs"))

  def sendLogsToServer[F[_]: Monad, A](logProgram: F[A])(using F: Listen[F, Chain[String]]): F[A] =
    logProgram.listen.flatMap {
      case (a, logs) => sendToServer[F](logs).as(a)
    }

  def logging[F[_]: Monad](using F: Tell[F, Chain[String]]): F[Unit] =
    // Example of some logging activity in your application
    for
      _ <- F.tell(Chain.one("First log"))
      _ <- F.tell(Chain.one("Second log"))
    yield
      ()
  val result = sendLogsToServer(logging[[X] =>> Writer[Chain[String], X]]).value
  println(result)
end ListenApp
