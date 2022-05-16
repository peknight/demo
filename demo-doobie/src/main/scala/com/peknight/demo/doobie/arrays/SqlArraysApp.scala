package com.peknight.demo.doobie.arrays

import cats.*
import cats.data.*
import cats.effect.*
import cats.implicits.*
import com.peknight.demo.doobie.xa
import doobie.*
import doobie.implicits.*
import doobie.postgres.*
import doobie.postgres.implicits.*

object SqlArraysApp extends IOApp.Simple:

  val y = xa.yolo
  import y.*

  val drop = sql"DROP TABLE IF EXISTS person".update.quick

  val create =
    sql"""
      CREATE TABLE person (
        id   SERIAL,
        name VARCHAR   NOT NULL UNIQUE,
        pets VARCHAR[] NOT NULL
      )
    """.update.quick

  def insert(name: String, pets: List[String]): ConnectionIO[Person] =
    sql"insert into person (name, pets) values ($name, $pets)"
      .update
      .withUniqueGeneratedKeys("id", "name", "pets")

  val run =
    for
      _ <- drop *> create
      _ <- insert("Bob", List("Nixon", "Slappy")).quick
      _ <- insert("Alice", Nil).quick
      // 万恶的null
      _ <- sql"select array['foo','bar','baz']".query[List[String]].quick
      _ <- sql"select array['foo','bar','baz']".query[Option[List[String]]].quick
      _ <- sql"select array['foo',NULL,'baz']".query[List[Option[String]]].quick
      _ <- sql"select array['foo',NULL,'baz']".query[Option[List[Option[String]]]].quick
    yield ()
