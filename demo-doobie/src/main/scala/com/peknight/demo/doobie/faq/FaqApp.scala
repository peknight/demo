package com.peknight.demo.doobie.faq

import cats.*
import cats.data.*
import cats.effect.*
import cats.effect.implicits.*
import cats.implicits.*
import com.peknight.demo.doobie.xa
import doobie.*
import doobie.implicits.*

import java.awt.geom.Point2D
import java.sql.SQLXML
import java.util.UUID

object FaqApp extends IOApp.Simple:

  val y = xa.yolo
  import y.*

  val s = "foo"

  def withoutTransaction[A](p: ConnectionIO[A]): ConnectionIO[A] =
    FC.setAutoCommit(true).bracket(_ => p)(_ => FC.setAutoCommit(false))

  def cities(code: Code, asc: Boolean): Query0[City] =
    val ord = if asc then fr"ASC" else fr"DESC"
    val sql =
      fr"""
        SELECT countrycode, name, population
        FROM city
        WHERE countrycode = $code
        ORDER BY name
      """ ++ ord
    sql.query[City]

  val join =
    sql"""
      select c.name, c.code, k.name, k.district
      from country c
      left outer join city k
      on c.capital = k.id
    """.query[(Country, Option[City2])]

  val run =
    for
      _ <- sql"select $s".query[String].check
      _ <- sql"select $s :: char".query[String].check
      _ <- cities(Code("USA"), true).check
      _ <- cities(Code("USA"), true).stream.take(5).quick
      _ <- cities(Code("USA"), false).stream.take(5).quick
      _ <- join.stream.filter(_._1.name.startsWith("United")).quick
    yield ()

