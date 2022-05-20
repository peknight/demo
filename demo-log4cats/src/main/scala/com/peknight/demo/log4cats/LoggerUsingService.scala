package com.peknight.demo.log4cats

import cats.Monad
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import org.typelevel.log4cats.LoggerFactory

// basic example of a service using LoggerFactory
class LoggerUsingService[F[_]: LoggerFactory: Monad] {

  val logger = LoggerFactory[F].getLogger

  def use(args: String): F[Unit] =
    for
      _ <- logger.info("yay! effect polymorphic code")
      _ <- logger.debug(s"and $args")
    yield ()
}
