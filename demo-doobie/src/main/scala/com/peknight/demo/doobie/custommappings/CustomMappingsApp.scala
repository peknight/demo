package com.peknight.demo.doobie.custommappings

import cats.*
import cats.data.*
import cats.effect.*
import cats.implicits.*
import com.peknight.demo.doobie.xa
import doobie.*
import doobie.implicits.*
import io.circe.*
import io.circe.jawn.*
import io.circe.syntax.*
import org.postgresql.util.PGobject

import java.awt.Point

object CustomMappingsApp extends IOApp.Simple:

  // def nope(msg: String, ex: Exception): ConnectionIO[Int] =
  //   sql"INSERT INTO log (message, detail) VALUES ($msg, $ex)".update.run

  // sql"SELECT message, detail FROM log".query[LogEntry]

  val run =
    for
      _ <- IO.unit
    yield ()
