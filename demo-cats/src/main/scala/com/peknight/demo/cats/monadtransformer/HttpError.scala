package com.peknight.demo.cats.monadtransformer

import cats.data.EitherT

import scala.concurrent.Future

sealed abstract class HttpError

object HttpError {
  final case class NotFound(item: String) extends HttpError
  final case class BadRequest(msg: String) extends HttpError

  type FutureEither[A] = EitherT[Future, HttpError, A]
}