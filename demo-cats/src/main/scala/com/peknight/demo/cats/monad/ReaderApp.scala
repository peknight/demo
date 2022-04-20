package com.peknight.demo.cats.monad

import cats.data.Reader
import cats.syntax.applicative.*

object ReaderApp extends App:

  val catName: Reader[Cat, String] = Reader(_.name)
  println(catName.run(Cat("Garfield", "lasagne")))

  val greetKitty: Reader[Cat, String] = catName.map(name => s"Hello $name")
  println(greetKitty.run(Cat("Garfield", "lasagne")))

  val feedKitty: Reader[Cat, String] = Reader(cat => s"Have a nice bowl of ${cat.favoriteFood}")

  val greetAndFeed: Reader[Cat, String] =
    for
      greet <- greetKitty
      feed <- feedKitty
    yield s"$greet. $feed."

  println(greetAndFeed(Cat("Garfield", "lasagne")))
  println(greetAndFeed(Cat("Heathcliff", "junk food")))

  type DbReader[A] = Reader[Db, A]

  def findUsername(userId: Int): DbReader[Option[String]] = Reader(db => db.usernames.get(userId))

  def checkPassword(username: String, password: String): DbReader[Boolean] = Reader(db =>
    db.passwords.get(username).contains(password)
  )

  def checkLogin(userId: Int, password: String): DbReader[Boolean] =
    for
      usernameOption <- findUsername(userId)
      passwordOk <- usernameOption
        .map(username => checkPassword(username, password))
        .getOrElse(false.pure[DbReader])
    yield passwordOk

  val users = Map(
    1 -> "dade",
    2 -> "kate",
    3 -> "margo"
  )

  val passwords = Map(
    "dade" -> "zerocool",
    "kate" -> "acidburn",
    "margo" -> "secret"
  )

  val db = Db(users, passwords)

  println(checkLogin(1, "zerocool").run(db))
  println(checkLogin(4, "davinci").run(db))
