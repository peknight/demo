package com.peknight.demo.doobie.updating

import cats.*
import cats.data.*
import cats.effect.*
import cats.implicits.*
import com.peknight.demo.doobie.xa
import doobie.*
import doobie.implicits.*
import fs2.Stream

object UpdatingApp extends IOApp.Simple:

  val y = xa.yolo
  import y.*

  val drop = sql"""DROP TABLE IF EXISTS person""".update.run

  val create =
    sql"""
      CREATE TABLE person (
        id   SERIAL,
        name VARCHAR NOT NULL UNIQUE,
        age  SMALLINT
      )
    """.update.run

  def insert1(name: String, age: Option[Short]): Update0 =
    sql"insert into person (name, age) values ($name, $age)".update

  // 对于插入后查回的场景可以这样写，虽然这样写很烦人，但是所有数据库都会支持（不过获取最近主键的方法不一定是`lastval()`）
  def insert2(name: String, age: Option[Short]): ConnectionIO[Person] =
    for
      _  <- sql"insert into person (name, age) values ($name, $age)".update.run
      id <- sql"select lastval()".query[Long].unique
      p  <- sql"select id, name, age from person where id = $id".query[Person].unique
    yield p

  // h2数据库可以返回主键，用withUniqueGeneratedKeys就可以把语句数量降为2个
  def insert2_H2(name: String, age: Option[Short]): ConnectionIO[Person] =
    for
      id <- sql"insert into person (name, age) values ($name, $age)"
        .update
        .withUniqueGeneratedKeys[Int]("id")
      p  <- sql"select id, name, age from person where id = $id"
        .query[Person]
        .unique
    yield p

  // 而像postgres这类数据库就NB了，可以在插入时把所有列都返回回来，这样一个sql就搞定了
  def insert3(name: String, age: Option[Short]): ConnectionIO[Person] = {
    sql"insert into person (name, age) values ($name, $age)"
      .update
      // 方法预期只影响一行（否则会抛异常），把需要的列名指定在这。尽管这个api不好看，但是JDBC只能帮我们到这了。
      .withUniqueGeneratedKeys("id", "name", "age")
  }

  // 更新操作也可以这么用
  val up = sql"update person set age = age + 1 where age is not null".update
    // 这里不止会更新一行，所以不用withUniqueGeneratedKeys
    .withGeneratedKeys[Person]("id", "name", "age")

  type PersonInfo = (String, Option[Short])

  // 批量操作

  def insertMany(ps: List[PersonInfo]): ConnectionIO[Int] =
    val sql = "insert into person (name, age) values (?, ?)"
    Update[PersonInfo](sql).updateMany(ps)

  val data = List[PersonInfo](("Frank", Some(12)), ("Daddy", None))

  def insertMany2(ps: List[PersonInfo]): Stream[ConnectionIO, Person] =
    val sql = "insert into person (name, age) values (?, ?)"
    Update[PersonInfo](sql).updateManyWithGeneratedKeys[Person]("id", "name", "age")(ps)

  val data2 = List[PersonInfo](("Banjo", Some(39)), ("Skeeter", None), ("Jim-Bob", Some(12)))

  val run =
    for
      rows <- (drop, create).mapN(_ + _).transact(xa)
      _ <- IO.println(rows)
      aliceRows <- insert1("Alice", Some(12)).run.transact(xa)
      _ <- IO.println(aliceRows)
      _ <- insert1("Bob", None).quick
      _ <- sql"select id, name, age from person".query[Person].quick
      _ <- sql"update person set age = 15 where name = 'Alice'".update.quick
      _ <- sql"select id, name, age from person".query[Person].quick
      _ <- insert2("Jimmy", Some(42)).quick
      _ <- insert3("Elvis", None).quick
      _ <- IO.println("---")
      _ <- up.quick
      _ <- IO.println("---")
      _ <- up.quick
      _ <- IO.println("---")
      _ <- insertMany(data).quick
      _ <- IO.println("---")
      _ <- insertMany2(data2).quick
    yield ()


