package com.peknight.demo.cats.mtl

import cats.Applicative
import cats.mtl.syntax.handle.*
import cats.syntax.all.*
import cats.mtl.{Raise, Handle}

object HandleApp extends App:
  def parseNumber[F[_]: Applicative](in: String)(using F: Raise[F, String]): F[Int] =
    // this function might raise an error
    if in.matches("-?[0-9]+") then in.toInt.pure[F]
    else F.raise(show"'$in' could not be parsed as a number")

  def notRecovered[F[_]: Applicative](using F: Raise[F, String]): F[Boolean] =
    parseNumber[F]("foo").map(n => if n > 5 then true else false)

  def recovered[F[_]: Applicative](using F: Handle[F, String]): F[Boolean] =
    parseNumber[F]("foo")
      .handle[String](_ => 0)
      .map(n => if n > 5 then true else false)
  val err = notRecovered[[X] =>> Either[String, X]]
  println(err)
  val result = recovered[[X] =>> Either[String, X]]
  println(result)
end HandleApp
