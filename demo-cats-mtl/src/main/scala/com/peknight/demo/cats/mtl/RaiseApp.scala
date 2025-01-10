package com.peknight.demo.cats.mtl

import cats.Applicative
import cats.mtl.Raise
import cats.syntax.all.*

object RaiseApp extends App:
  def parseNumber[F[_]: Applicative](in: String)(using F: Raise[F, String]): F[Int] =
    if in.matches("-?[0-9]+") then in.toInt.pure[F]
    else F.raise(show"'$in' could not be parsed as a number'")
  val valid = parseNumber[[X] =>> Either[String, X]]("123")
  println(valid)
  val invalid = parseNumber[[X] =>> Either[String, X]]("123abc")
  println(invalid)
end RaiseApp
