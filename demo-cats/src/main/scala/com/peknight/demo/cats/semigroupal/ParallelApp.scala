package com.peknight.demo.cats.semigroupal

import cats.Semigroupal
import cats.arrow.FunctionK
import cats.syntax.apply.*
import cats.syntax.parallel.*

object ParallelApp extends App:
  type ErrorOr[A] = Either[Vector[String], A]
  val error1: ErrorOr[Int] = Left(Vector("Error 1"))
  val error2: ErrorOr[Int] = Left(Vector("Error 2"))

  println(Semigroupal[ErrorOr].product(error1, error2))
  println((error1, error2).tupled)
  println((error1, error2).parTupled)

  type ErrorOrList[A] = Either[List[String], A]
  val errStr1: ErrorOrList[Int] = Left(List("error 1"))
  val errStr2: ErrorOrList[Int] = Left(List("error 2"))
  println((errStr1, errStr2).parTupled)

  val success1: ErrorOr[Int] = Right(1)
  val success2: ErrorOr[Int] = Right(2)
  val addTwo = (x: Int, y: Int) => x + y

  println((error1, error2).parMapN(addTwo))
  println((success1, success2).parMapN(addTwo))

  object optionToList extends FunctionK[Option, List]:
    def apply[A](fa: Option[A]): List[A] = fa match
      case None => List.empty[A]
      case Some(a) => List(a)

  println(optionToList(Some(1)))
  println(optionToList(None))


  println((List(1, 2), List(3, 4)).tupled) // List((1, 3), (1, 4), (2, 3), (2, 4))
  println((List(1, 2), List(3, 4)).parTupled) // List((1, 3), (2, 4))
