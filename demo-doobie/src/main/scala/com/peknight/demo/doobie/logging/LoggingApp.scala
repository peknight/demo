package com.peknight.demo.doobie.logging

import cats.*
import cats.effect.*
import cats.implicits.*
import com.peknight.demo.doobie.transactorWithLogHandler
import doobie.*
import doobie.implicits.*
import doobie.util.log.LogEvent

object LoggingApp extends IOApp.Simple:

  val printSqlLogHandler: LogHandler[IO] =
    new LogHandler[IO]:
      def run(logEvent: LogEvent): IO[Unit] = IO.println(logEvent.sql)

  val xa = transactorWithLogHandler(Some(printSqlLogHandler))

  def byName(pattern: String) =
    sql"select name, code from country where name like $pattern"
      .query[(String, String)]
      .to[List]
      .transact(xa)

  // 隐式引入
  // 旧版写法 given LogHandler = LogHandler.jdkLogHandler
  // val nop = LogHandler(_ => ())
  // val trivial = LogHandler(e => Console.println(s"*** $e"))

  def byNameWithLabel(pattern: String) =
    sql"select name, code from country where name like $pattern"
      .queryWithLabel[(String, String)]("select_country_by_pattern")
      .to[List]
      .transact(xa)

  def users = List.range(0, 4).map(n => s"user-$n")


  val run =
    for
      uCountries <- byName("U%")
      _ <- IO.println(uCountries)
      uCountries2 <- byNameWithLabel("U%")
      _ <- IO.println(uCountries2)
      // define an IOLocal where we store the user which caused the query to be run
      currentUser <- IOLocal("")
      // store all successful sql here, for all users
      successLogsRef <- Ref[IO].of(List.empty[String])
      xa1 = Transactor.fromDriverManager[IO](
        driver = "org.h2.Driver",
        url = "jdbc:h2:mem:queryspec;DB_CLOSE_DELAY=-1",
        user = "sa",
        password = "",
        logHandler = Some(new LogHandler[IO] {
          def run(logEvent: LogEvent): IO[Unit] =
            currentUser.get.flatMap(user => successLogsRef.update(logs => s"sql for $user: '${logEvent.sql}'" :: logs))
        })
      )
      // run a bunch of queries
      _ <- users.parTraverse { user =>
        for
          _ <- currentUser.set(user)
          _ <- sql"select 1".query[Int].unique.transact(xa1)
        yield ()
      }
      logs <- successLogsRef.get
      _ <- IO.println(logs)
      // _ <- sql"select 42".queryWithLogHandler[Int](trivial).unique.transact(xa)
    yield ()
