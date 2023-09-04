package com.peknight.demo.log4cats

import cats.Applicative
import cats.effect.*
import cats.effect.std.Console
import cats.implicits.*
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.syntax.*
import org.typelevel.log4cats.{Logger, SelfAwareStructuredLogger}

object Log4CatsApp extends IOApp.Simple:

  // 这样写才是纯函数，但是不那么方便
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

  def successComputation[F[_]: Applicative]: F[Int] = Applicative[F].pure(1)
  def errorComputation[F[_]: Sync]: F[Unit] = Sync[F].raiseError[Unit](new Throwable("Sorry!"))

  def logLaconicSyntax[F[_]: Sync: Logger] =
    for
      result1 <- successComputation[F]
      _ <- info"First result is $result1"
      _ <- errorComputation[F].onError {
        case _ => error"We got an error!"
      }
    yield ()

  // 已过时
  // create our LoggerFactory
  // given LoggerFactory[IO] = Slf4jFactory[IO]
  // 依赖org.typelevel.log4cats.slf4j.loggerFactoryforSync
  // val ioLogger: SelfAwareStructuredLogger[IO] = LoggerFactory[IO].getLogger
  // or
  // def anyFSyncLogger[F[_]: Sync]: SelfAwareStructuredLogger[F] = Slf4jFactory[F].getLogger

  val run =
    for
      _ <- MyThing.doSomething[IO]
      _ <- safelyDoThings[IO]
      logger <- Slf4jLogger.create[IO]
      given Logger[IO] = logger
      _ <- passForEasierUse[IO]
      _ <- logLaconicSyntax[IO].recoverWith(_ => IO.unit)
      // we summon LoggerFactory instance, and create logger
      // _ <- ioLogger.info("logging in IO!"): IO[Unit]
      _ <- new LoggerUsingService(logger).use("foo")
    yield ()

