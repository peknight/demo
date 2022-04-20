package com.peknight.demo.cats.monad

import cats.MonadError
import cats.syntax.applicative.*
import cats.syntax.applicativeError.*
import cats.syntax.monadError.*

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

object MonadErrorApp extends App:

  type ErrorOr[A] = Either[String, A]

  val monadError = MonadError[ErrorOr, String]

  val success1 = monadError.pure(42)
  val failure1 = monadError.raiseError[String]("Badness")

  println(monadError.handleErrorWith(failure1) {
    case "Badness" => monadError.pure("It's ok")
    case _ => monadError.raiseError("It's not ok")
  })

  val failure2 = monadError.raiseError[Int]("Badness")
  println(monadError.handleError(failure2) {
    case "Badness" => 42
    case _ => -1
  })

  println(monadError.ensure(success1)("Number too low!")(_ > 1000))

  val success2 = 42.pure[ErrorOr]
  val failure3 = "Badness".raiseError[ErrorOr, Int]
  println(failure3.handleErrorWith {
    case "Badness" => 256.pure[ErrorOr]
    case _ => ("It's not ok").raiseError[ErrorOr, Int]
  })
  println(success2.ensure("Number too low!")(_ > 1000))

  val exn: Throwable = new RuntimeException("It's all gone wrong")

  println(exn.raiseError[Try, Int])
  println(exn.raiseError[Future, Int])

  def validateAdult[F[_]](age: Int)(using me: MonadError[F, Throwable]): F[Int] =
    if age >= 18 then age.pure[F]
    else new IllegalArgumentException("Age must be greater than or equal to 18").raiseError[F, Int]

  println(validateAdult[Try](18))
  println(validateAdult[Try](8))

  type ExceptionOr[A] = Either[Throwable, A]
  println(validateAdult[ExceptionOr](-1))
