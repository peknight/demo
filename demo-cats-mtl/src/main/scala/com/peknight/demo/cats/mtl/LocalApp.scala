package com.peknight.demo.cats.mtl

import cats.{Applicative, Monad}
import cats.data.Reader
import cats.mtl.{Ask, Local}
import cats.syntax.all.*

object LocalApp extends App:
  def calculateContentLength[F[_]: Applicative](using F: Ask[F, String]): F[Int] = F.ask.map(_.length)
  def calculateModifiedContentLength[F[_]: Applicative](using F: Local[F, String]): F[Int] =
    F.local(calculateContentLength[F])(e => s"Prefix $e")
  val result = calculateModifiedContentLength[[X] =>> Reader[String, X]].run("Hello")
  println(result)
  def both[F[_]: Monad](using F: Local[F, String]): F[(Int, Int)] =
    for
      length <- calculateContentLength[F]
      modifiedLength <- calculateModifiedContentLength[F]
    yield
      (length, modifiedLength)
  val res = both[[X] =>> Reader[String, X]].run("Hello")
  println(res)
end LocalApp
