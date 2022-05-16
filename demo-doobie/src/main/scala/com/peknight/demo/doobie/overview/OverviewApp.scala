package com.peknight.demo.doobie.overview

import cats.effect.{IO, IOApp}
import doobie.*
import doobie.implicits.*

object OverviewApp extends IOApp.Simple:

  val xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver", "jdbc:postgresql:world", "postgres", ""
  )

  def find(n: String): ConnectionIO[Option[Country]] =
    sql"select code, name, population from country where name = $n".query[Country].option

  val run =
    for
      franceOption <- find("France").transact(xa)
      _ <- IO.println(franceOption)
    yield ()

