package com.peknight.demo.log4cats

import cats.effect.*
import cats.effect.std.Console
import cats.implicits.*
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Log4CatsApp:
  def safelyDoThings[F[_]: Sync: Console]: F[Unit] =
    for
      logger <- Slf4jLogger.create[F]
      _ <- logger.info("Logging at start of safelyDoThings")
      something <- Console[F].println("I could do anything").onError {
        case e => logger.error(e)("Something Went Wrong in safelyDoThings")
      }
      _ <- logger.info("Logging at end of safelyDoThings")
    yield something

  def passForEasierUse[F[_]: Sync: Logger: Console] =
    for
      _ <- Logger[F].info("Logging at start of passForEasierUse")
      something <- Console[F].println("I could do anything").onError {
        case e => Logger[F].error(e)("Something Went Wrong in passForEasierUse")
      }
      _ <- Logger[F].info("Logging at end of passForEasierUse")
    yield something


