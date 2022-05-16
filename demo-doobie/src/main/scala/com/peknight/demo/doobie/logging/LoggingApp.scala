package com.peknight.demo.doobie.logging

import cats.*
import cats.data.*
import cats.effect.*
import cats.implicits.*
import com.peknight.demo.doobie.xa
import doobie.*
import doobie.implicits.*

object LoggingApp extends IOApp.Simple:

  def byName(pat: String) =
    sql"select name, code from country where name like $pat"
      .queryWithLogHandler[(String, String)](LogHandler.jdkLogHandler)
      .to[List]
      .transact(xa)

  // 隐式引入
  given LogHandler = LogHandler.jdkLogHandler
  def byName2(pat: String) =
    sql"select name, code from country where name like $pat"
      .query[(String, String)]
      .to[List]
      .transact(xa)

  val nop = LogHandler(_ => ())

  val trivial = LogHandler(e => Console.println(s"*** $e"))

  val run =
    for
      uCountries <- byName("U%")
      _ <- IO.println(uCountries)
      uCountries2 <- byName2("U%")
      _ <- IO.println(uCountries2)
      _ <- sql"select 42".queryWithLogHandler[Int](trivial).unique.transact(xa)
    yield ()
