package com.peknight.demo.cats.mtl

import cats.Monad
import cats.data.{Ior, NonEmptyChain}
import cats.mtl.Chronicle
import cats.syntax.all.*

object ChronicleApp extends App:
  type Failures = NonEmptyChain[String]
  case class Username(value: String)
  case class Password(value: String)
  case class User(name: Username, pw: Password)
  def validateUsername[F[_]: Monad](u: String)(using F: Chronicle[F, Failures]): F[Username] =
    if u.isEmpty then F.confess(NonEmptyChain.one("Can't be empty"))
    else if u.contains(".") then F.dictate(NonEmptyChain.one("Dot in name is deprecated")).map(_ => Username(u))
    else Username(u).pure[F]
  def validatePassword[F[_]: Monad](p: String)(using F: Chronicle[F, Failures]): F[Password] =
    if p.length < 8 then F.confess(NonEmptyChain.one("Password too short"))
    else if p.length < 10 then F.dictate(NonEmptyChain.one("Password should be longer")).map(_ => Password(p))
    else Password(p).pure[F]
  def validateUser[F[_]: Monad](name: String, password: String)(using F: Chronicle[F, Failures]): F[User] =
    (validateUsername[F](name), validatePassword[F](password)).mapN(User.apply)

  val luka = validateUser[[X] =>> Ior[Failures, X]]("Luka", "secret")
  println(luka)
  val john = validateUser[[X] =>> Ior[Failures, X]]("john.doe", "secret123")
  println(john)
  val jane = validateUser[[X] =>> Ior[Failures, X]]("jane", "reallysecurepassword")
  println(jane)
end ChronicleApp
