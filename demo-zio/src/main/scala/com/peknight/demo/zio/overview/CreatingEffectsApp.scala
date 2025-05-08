package com.peknight.demo.zio.overview

import zio.*

import java.io.IOException
import java.net.{ServerSocket, Socket}
import scala.concurrent.{ExecutionContext, Future}
import scala.io.{Codec, Source, StdIn}
import scala.util.Try

object CreatingEffectsApp:
  // From Values
  val s1: UIO[Int] = ZIO.succeed(42)

  // From Failure Values
  val f1: IO[String, Nothing] = ZIO.fail("Uh oh!")
  val f2: IO[Exception, Nothing] = ZIO.fail(new Exception("Uh oh!"))

  // From Scala Values - Option
  val zOption: IO[Option[Nothing], Int] = ZIO.fromOption(Some(2))
  val zOption2: IO[String, Int] = zOption.orElseFail("It wasn't there")

  val maybeId: IO[Option[Nothing], String] = ZIO.fromOption(Some("abc123"))
  case class User(teamId: String)
  def getUser(userId: String): Task[Option[User]] = ???
  case class Team()
  def getTeam(teamId: String): Task[Team] = ???

  val result: Task[Option[(User, Team)]] =
    (for
      id <- maybeId
      user <- getUser(id).some
      team <- getTeam(user.teamId).asSomeError
    yield (user, team)).unsome

  // From Scala Values - Either
  val zEither: UIO[String] = ZIO.fromEither(Right("Success!"))

  // From Scala Values - Try
  val zTry: Task[Int] = ZIO.fromTry(Try(42 / 0))

  // From Scala Values - Future
  lazy val future: Future[String] = Future.successful("Hello!")
  val zFuture: Task[String] = ZIO.fromFuture { ec =>
    given ExecutionContext = ec
    future.map(_ => "Goodbye!")
  }

  // From Code - Synchronous Code
  val readLine: Task[String] = ZIO.attempt(StdIn.readLine())
  def printLine(line: String): UIO[Unit] = ZIO.succeed(println(line))
  val readLine2: IO[IOException, String] = ZIO.attempt(StdIn.readLine()).refineToOrDie[IOException]

  // From Code - Asynchronous Code
  case class AuthError()
  object legacy:
    def login(onSuccess: User => Unit, onFailure: AuthError => Unit): Unit = ???
  end legacy

  val login: IO[AuthError, User] = ZIO.async[Any, AuthError, User] { callback =>
    legacy.login(
      user => callback(ZIO.succeed(user)),
      err => callback(ZIO.fail(err))
    )
  }

  // Blocking Synchronous Code
  def download(url: String): Task[String] = ZIO.attempt {
    Source.fromURL(url)(using Codec.UTF8).mkString
  }

  def safeDownload(url: String): Task[String] = ZIO.blocking(download(url))

  def sleeping: Task[Unit] = ZIO.attemptBlocking(Thread.sleep(Long.MaxValue))

  def accept(l: ServerSocket): Task[Socket] = ZIO.attemptBlockingCancelable(l.accept())(ZIO.succeed(l.close()))
