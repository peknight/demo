package com.peknight.demo.doobie.postgresext

import cats.*
import cats.data.*
import cats.effect.*
import cats.implicits.*
import com.peknight.demo.doobie.xa
import doobie.*
import doobie.implicits.*
// Provides mappings for java.time.Instant
import doobie.implicits.legacy.instant.*
// Provides mappings for java.time.LocalDate
import doobie.implicits.legacy.localdate.*
import doobie.postgres.*
// postgres json（circe）支持
import doobie.postgres.circe.json.implicits
import doobie.postgres.circe.jsonb.implicits
import doobie.postgres.implicits.*
// postgis 支持
import doobie.postgres.pgisimplicits.*

object PostgresExtensionsApp extends IOApp.Simple:

  given CanEqual[SqlState, SqlState] = CanEqual.derived

  given Meta[MyEnum.Value] = pgEnum(MyEnum, "myenum")

  given Meta[MyJavaEnum] = pgJavaEnum("myenum")

  given Meta[FooBar] = pgEnumStringOpt("myenum", FooBar.fromEnum, FooBar.toEnum)

  // 错误处理，这里sql虽然不合法可以报红，但是不会影响编译
  val p = sql"oops".query[String].unique

  // 这个也很强啊
  val q =
    """
      copy country (name, code, population)
      to stdout (
        encoding 'utf-8',
        force_quote *,
        format csv
      )
    """
  // return value is the row count
  val prog: ConnectionIO[Long] = PHC.pgGetCopyAPI(PFCM.copyOut(q, Console.out))

  val create: ConnectionIO[Unit] =
    sql"""
      CREATE TEMPORARY TABLE food (
        name       VARCHAR,
        vegetarian BOOLEAN,
        calories   INTEGER
      )
    """.update.run.void

  val foods = List(
    Food("banana", true, 110),
    Food("cheddar cheese", true, 113),
    Food("Big Mac", false, 1120)
  )

  def insert[F[_]: Foldable](fa: F[Food]): ConnectionIO[Long] =
    sql"COPY food (name, vegetarian, calories) FROM STDIN".copyIn(fa)

  val run =
    for
      res <- sql"select 'foo'::myenum".query[MyEnum.Value].unique.transact(xa)
      _ <- IO.println(res)
      javaRes <- sql"select 'foo'::myenum".query[MyJavaEnum].unique.transact(xa)
      _ <- IO.println(javaRes)
      fooBarRes <- sql"select 'foo'::myenum".query[FooBar].unique.transact(xa)
      _ <- IO.println(fooBarRes)
      // attempt is provided by ApplicativeError instance
      attemptRes <- p.attempt.transact(xa)
      _ <- IO.println(attemptRes)
      // this catches only SQL exceptions
      attemptSqlStateRes <- p.attemptSqlState.transact(xa)
      _ <- IO.println(attemptSqlStateRes)
      // catch it
      attemptSomeSqlStateRes1 <- p.attemptSomeSqlState { case SqlState("42601") => "caught!" } .transact(xa)
      _ <- IO.println(attemptSomeSqlStateRes1)
      // same, w/constant
      attemptSomeSqlStateRes2 <- p.attemptSomeSqlState { case sqlstate.class42.SYNTAX_ERROR => "caught!" } .transact(xa)
      _ <- IO.println(attemptSomeSqlStateRes2)
      // recover
      exceptSomeSqlStateRes <- p.exceptSomeSqlState {
        case sqlstate.class42.SYNTAX_ERROR => "caught!".pure[ConnectionIO]
      } .transact(xa)
      _ <- IO.println(exceptSomeSqlStateRes)
      // using recovery combinator
      syntaxErrorRes <- p.onSyntaxError("caught!".pure[ConnectionIO]).transact(xa)
      _ <- IO.println(syntaxErrorRes)
      progRes <- prog.transact(xa)
      _ <- IO.println(progRes)
      insertFoodsRes <- (create *> insert(foods)).transact(xa)
      _ <- IO.println(insertFoodsRes)
      explainRes <- sql"select name from country".query[String].explain.transact(xa)
      _ <- explainRes.traverse(IO.println)
      explainAnalyzeRes <- sql"select name from country".query[String].explainAnalyze.transact(xa)
      _ <- explainAnalyzeRes.traverse(IO.println)
    yield ()
