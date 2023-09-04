package com.peknight.demo.log4cats

import cats.Monad
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import org.typelevel.log4cats.Logger

// basic example of a service using LoggerFactory 已过时 LoggerFactory不再传入
class LoggerUsingService[F[_]: Monad](logger: Logger[F]) {

  // val logger = LoggerFactory[F].getLogger

  def use(args: String): F[Unit] =
    for
      _ <- logger.info("yay! effect polymorphic code")
      _ <- logger.debug(s"and $args")
    yield ()
}
