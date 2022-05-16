package com.peknight.demo.doobie.selecting

import cats.*
import cats.data.*
import cats.effect.*
import cats.implicits.*
import com.peknight.demo.doobie.xa
import doobie.*
import doobie.implicits.*
import fs2.Stream

object SelectingApp extends IOApp.Simple:

  val countryNameList = sql"select name from country"
    .query[String]    // Query0[String]
    .to[List]         // ConnectionIO[List[String]]
    .transact(xa)     // IO[List[String]]

  val countryNameStream = sql"select name from country"
    .query[String]    // Query0[String]
    .stream           // Stream[ConnectionIO, String]
    .take(5)          // Stream[ConnectionIO, String]
    .compile.toList   // ConnectionIO[List[String]]
    .transact(xa)     // IO[List[String]]

  // YOLO mode (You only live once?)
  val y = xa.yolo
  import y.*

  // 将查询的结果流直接透出（Stream[ConnectionIO, Country2]这样的流外部没法直接用，需要变成Stream[IO, Country2]）
  val p: Stream[IO, Country2] =
    sql"select name, population, gnp from country"
      .query[Country2] // Query0[Country2]
      .stream          // Stream[ConnectionIO, Country2]
      .transact(xa)    // Stream[IO, Country2]

  // sql"..." 实际上是下面这种写法的一种语法糖
  val proc = HC.stream[(Code, Country2)](
    "select code, name, population, gnp from country", // statement
    ().pure[PreparedStatementIO],                      // prep (none) 设置查询参数，本节sql没参数，所以是个Unit
    512                                                // chunk size
  )

  val run =
    for
      nameList <- countryNameList
      _ <- IO.println(nameList.take(5))
      nameStream <- countryNameStream
      _ <- IO.println(nameStream)
      _ <- sql"select name from country"
        .query[String] // Query0[String]
        .stream        // Stream[ConnectionIO, String]
        .take(5)       // Stream[ConnectionIO, String]
        .quick         // IO[Unit]
      // Multi-Column queries
      _ <- sql"select code, name, population, gnp from country"
        .query[(String, String, Int, Option[Double])]
        .stream
        .take(5)
        .quick
      _ <- sql"select code, name, population, gnp from country"
        .query[String *: String *: Int *: Option[Double] *: EmptyTuple]
        .stream
        .take(5)
        .quick
      _ <- sql"select code, name, population, gnp from country"
        .query[Country]
        .stream
        .take(5)
        .quick
      // 玩个花的
      _ <- sql"select code, name, population, gnp from country"
        .query[(Code, Country2)]
        .stream
        .take(5)
        .quick
      // 使用code作key
      _ <- sql"select code, name, population, gnp from country"
        .query[(Code, Country2)]
        .stream.take(5)
        .compile.toList
        .map(_.toMap)
        .quick
      ps <- p.take(5).compile.toVector
      _ <- IO.println(ps)
      _ <- proc.take(5)        // Stream[ConnectionIO, (Code, Country2)]
        .compile.toList // ConnectionIO[List[(Code, Country2)]]
        .map(_.toMap)   // ConnectionIO[Map[Code, Country2]]
        .quick
    yield ()
