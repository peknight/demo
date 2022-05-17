package com.peknight.demo.doobie.h2ext

import cats.effect.*
import cats.implicits.*
import doobie.*
import doobie.h2.*
import doobie.implicits.*

object H2App extends IOApp.Simple:

  val transactor: Resource[IO, H2Transactor[IO]] =
    for
      ce <- ExecutionContexts.fixedThreadPool[IO](32)
      xa <- H2Transactor.newH2Transactor[IO]("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "", ce)
    yield xa

  //noinspection DuplicatedCode
  val run = transactor.use { xa =>
    for
      n <- sql"select 42".query[Int].unique.transact(xa)
      _ <- IO.println(n)
    yield ()
  }



