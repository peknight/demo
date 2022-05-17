package com.peknight.demo.doobie.custommappings

import cats.*
import cats.data.*
import cats.effect.*
import cats.implicits.*
import com.peknight.demo.doobie.custommappings.NatModule.{*, given}
import com.peknight.demo.doobie.xa
import doobie.*
import doobie.implicits.*
import io.circe.*
import io.circe.jawn.*
import io.circe.syntax.*
import org.postgresql.util.PGobject

import java.awt.Point

/**
 * Get 取单字段
 * Put 设置单字段参数
 * Meta提供 Get和Put的对偶
 * Read 取多字段
 * Write 设置多字段参数
 * Read & Write没有提供对偶
 */
object CustomMappingsApp extends IOApp.Simple:

  // def nope(msg: String, ex: Exception): ConnectionIO[Int] =
  //   sql"INSERT INTO log (message, detail) VALUES ($msg, $ex)".update.run

  // sql"SELECT message, detail FROM log".query[LogEntry]

  given Show[PGobject] = Show.show(_.getValue.take(250))

  val jsonGet: Get[Json] = Get.Advanced.other[PGobject](NonEmptyList.of("json")).temap[Json] { o =>
    parse(o.getValue).leftMap(_.show)
  }

  val jsonPut: Put[Json] = Put.Advanced.other[PGobject](NonEmptyList.of("json")).tcontramap[Json] { j =>
    val o = new PGobject
    o.setType("json")
    o.setValue(j.noSpaces)
    o
  }

  given Meta[Json] = Meta.Advanced.other[PGobject]("json")
    .timap[Json](a => parse(a.getValue).leftMap[Json](e => throw e).merge){a =>
      val o = new PGobject
      o.setType("json")
      o.setValue(a.noSpaces)
      o
    }

  given Read[Point] = Read[(Int, Int)].map { case (x, y) => new Point(x, y) }
  given Write[Point] = Write[(Int, Int)].contramap { p => (p.x, p.y) }

  val run =
    for
      _ <- IO.unit
    yield ()
