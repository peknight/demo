package com.peknight.demo.doobie.connecting

import cats.*
import cats.data.Kleisli
import cats.effect.*
import cats.implicits.*
import com.peknight.demo.doobie.xa
import doobie.*
import doobie.free.KleisliInterpreter
import doobie.free.connection.ConnectionOp
import doobie.implicits.*

import java.sql.Connection

object ConnectingApp extends IOApp.Simple:

  val program1 = 42.pure[ConnectionIO]

  val program2 = sql"select 42".query[Int].unique

  val program3: ConnectionIO[(Int, Double)] =
    for
      a <- sql"select 42".query[Int].unique
      b <- sql"select random()".query[Double].unique
    yield (a, b)

  // 对于program3来说 monad没必要，applicative functor足够了
  val program3a =
    val a: ConnectionIO[Int] = sql"select 42".query[Int].unique
    val b: ConnectionIO[Double] = sql"select random()".query[Double].unique
    (a, b).tupled

  val valuesList = program3a.replicateA(5)

  val interpreter = KleisliInterpreter[IO](LogHandler.noop).ConnectionInterpreter

  val kleisli = program1.foldMap(interpreter)

  val io3 = IO(null: java.sql.Connection) >>= kleisli.run

  val run =
    for
      p1 <- program1.transact(xa)
      _ <- IO.println(p1)
      p2 <- program2.transact(xa)
      _ <- IO.println(p2)
      p3 <- program3.transact(xa)
      _ <- IO.println(p3)
      p3a <- program3a.transact(xa)
      _ <- IO.println(p3a)
      vl <- valuesList.transact(xa)
      _ <- IO.println(vl)
      i3 <- io3
      _ <- IO.println(i3)
    yield ()

