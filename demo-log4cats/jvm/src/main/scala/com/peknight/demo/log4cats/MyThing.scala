package com.peknight.demo.log4cats

import cats.effect.*
import cats.effect.std.Console
import cats.implicits.*
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object MyThing:

  given logger[F[_]: Sync]: Logger[F] = Slf4jLogger.getLogger[F]

  def doSomething[F[_]: Sync: Console]: F[Unit] =
    Logger[F].info("Logging Start Something") *>
      Console[F].println("I could be doing anything").attempt.flatMap {
        case Left(e) => Logger[F].error(e)("Something Went Wrong")
        case Right(_) => Sync[F].pure(())
      }

