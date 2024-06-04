package com.peknight.demo.zio.reference.core

import zio.*

object ZIOApp extends ZIOAppDefault:

  val s1 = ZIO.succeed(42)

  val f1 = ZIO.fail("Uh oh!")

  val f2 = ZIO.fail(new Exception("Uh oh!"))

  val zoption: IO[Option[Nothing], Int] = ZIO.fromOption(Some(2))

  val zoption2: IO[String, Int] = zoption.mapError(_ => "It wasn't there!")

  case class User(teamId: String)
  case class Team()
  val maybeId: IO[Option[Nothing], String] = ZIO.fromOption(Some("abc123"))
  def getUser(userId: String): IO[Throwable, Option[User]] = ???
  def getTeam(teamId: String): IO[Throwable, Team] = ???
  def result: IO[Throwable, Option[(User, Team)]] = (
    for
      id <- maybeId
      user <- getUser(id).some
      team <- getTeam(user.teamId).asSomeError
    yield (user, team)
  ).unsome

  def run: ZIO[Any & ZIOAppArgs & Scope, Any, Any] =
    for
      s1Res <- s1
      _ <- Console.printLine(s1Res)
      f1Res <- f1.either
      _ <- Console.printLine(f1Res)
      f2Res <- f2.either
      _ <- Console.printLine(f2Res)
      zoptionRes <- zoption
      _ <- Console.printLine(zoptionRes)
      zoption2Res <- zoption
      _ <- Console.printLine(zoption2Res)
    yield ()
end ZIOApp
