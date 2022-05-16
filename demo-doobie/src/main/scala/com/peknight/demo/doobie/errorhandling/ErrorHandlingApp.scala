package com.peknight.demo.doobie.errorhandling

import cats.*
import cats.data.*
import cats.effect.*
import cats.effect.unsafe.implicits.global
import cats.implicits.*
import com.peknight.demo.doobie.xa
import doobie.*
import doobie.implicits.*
import doobie.postgres.*

object ErrorHandlingApp extends IOApp.Simple:

  given CanEqual[SqlState, SqlState] = CanEqual.derived

  val y = xa.yolo
  import y.*

  val p = 42.pure[ConnectionIO]
  val ep: ConnectionIO[Either[Throwable, Int]] = p.attempt
  // `attemptSql`像`attempt`但只处理SQLException
  // `attemptSomeSql`只处理指定的SQLException
  // `exceptSql`从SQLException中恢复为一个新action
  // `onSqlException`当出现SQLException时执行一个action并忽略它的结果
  // `attemptSqlState`像`attemptSql`但会交出`M[Either[SQLState, A]]`
  // `attemptSomeSqlState`像处理指定的`SQLState`
  // `exceptSqlState`从`SQLState`中恢复为一个新action
  // `exceptSomeSqlState`从指定`SQLState`中恢复为一个新action

  def insert(s: String): ConnectionIO[Person] =
    sql"insert into person (name) values ($s)"
      .update.withUniqueGeneratedKeys("id", "name")

  val secondInsert = IO {
    try insert("bob").quick.unsafeRunSync()
    catch
      case e: java.sql.SQLException =>
        println(e.getMessage)
        println(e.getSQLState)
  }

  def safeInsert(s: String): ConnectionIO[Either[String, Person]] =
    insert(s).attemptSomeSqlState {
      case sqlstate.class23.UNIQUE_VIOLATION => "Oops!"
    }

  val run =
    for
      _ <- List(sql"""DROP TABLE IF EXISTS person""",
        sql"""CREATE TABLE person (
          id    SERIAL,
          name  VARCHAR NOT NULL UNIQUE)
        """).traverse(_.update.quick).void
      _ <- insert("bob").quick
      _ <- secondInsert
      _ <- safeInsert("bob").quick
      _ <- safeInsert("steve").quick
    yield ()

