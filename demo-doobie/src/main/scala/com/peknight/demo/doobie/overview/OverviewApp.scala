package com.peknight.demo.doobie.overview

import cats.effect.{IO, IOApp}
import com.peknight.demo.doobie.xa
import doobie.*
import doobie.implicits.*

object OverviewApp extends IOApp.Simple:

  def find(n: String): ConnectionIO[Option[Country]] =
    sql"select code, name, population from country where name = $n".query[Country].option

  val run = find("France").transact(xa).flatMap(IO.println)

