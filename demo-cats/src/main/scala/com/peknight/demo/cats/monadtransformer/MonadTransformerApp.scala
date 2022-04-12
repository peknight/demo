package com.peknight.demo.cats.monadtransformer

import cats.data.{EitherT, OptionT, Writer}
import cats.syntax.applicative._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Try

object MonadTransformerApp extends App {
  type ListOption[A] = OptionT[List, A]
  val result1: ListOption[Int] = OptionT(List(Option(10)))
  println(result1)
  val result2: ListOption[Int] = 32.pure[ListOption]
  println(result2)
  val res1 = result1.flatMap { x =>
    result2.map { y =>
      x + y
    }
  }
  println(res1)

  type ErrorOr[A] = Either[String, A]

  type ErrorOrOption[A] = OptionT[ErrorOr, A]

  val a = 10.pure[ErrorOrOption]
  val b = 32.pure[ErrorOrOption]

  val c = a.flatMap(x => b.map(y => x + y))

  println(c)

  type FutureEither[A] = EitherT[Future, String, A]
  type FutureEitherOption[A] = OptionT[FutureEither, A]

  val futureEitherOr: FutureEitherOption[Int] =
    for {
      a <- 10.pure[FutureEitherOption]
      b <- 32.pure[FutureEitherOption]
    } yield a + b

  println(Await.result(futureEitherOr.value.value, 1.second))

  // kind projector
  123.pure[EitherT[Option, String, *]]

  val errorStack1 = OptionT[ErrorOr, Int](Right(Some(10)))
  println(errorStack1.value)

  val errorStack2 = 32.pure[ErrorOrOption]
  println(errorStack2.value.map(_.getOrElse(-1)))

  type Logged[A] = Writer[List[String], A]

  def parseNumber(str: String): Logged[Option[Int]] = Try(str.toInt).toOption match {
    case Some(num) => Writer(List(s"Read $str"), Some(num))
    case None => Writer(List(s"Failed on $str"), None)
  }

  def addAll(a: String, b: String, c: String): Logged[Option[Int]] = {
    val result = for {
      a <- OptionT(parseNumber(a))
      b <- OptionT(parseNumber(b))
      c <- OptionT(parseNumber(c))
    } yield a + b + c

    result.value
  }

  val resultAddAll1 = addAll("1", "2", "3")
  println(resultAddAll1)

  val resultAddAll2 = addAll("1", "a", "3")
  println(resultAddAll2)

  type Response[A] = EitherT[Future, String, A]

  val powerLevels = Map(
    "Jazz" -> 6,
    "Bumblebee" -> 8,
    "Hot Rod" -> 10
  )

  def getPowerLevel(ally: String): Response[Int] = {
    powerLevels.get(ally) match {
      case Some(avg) => EitherT.right(Future(avg))
      case None => EitherT.left(Future(s"$ally unreachable"))
    }
  }

  def canSpecialMove(ally1: String, ally2: String): Response[Boolean] = for {
    ally1Level <- getPowerLevel(ally1)
    ally2Level <- getPowerLevel(ally2)
  } yield ally1Level + ally2Level > 15

  def tacticalReport(ally1: String, ally2: String): String = {
    val stack = canSpecialMove(ally1, ally2).value
    val future = stack.map {
      case Right(true) => s"$ally1 and $ally2 are ready to roll out!"
      case Right(false) => s"$ally1 and $ally2 need a recharge."
      case Left(msg) => s"Comms error: $msg"
    }
    Await.result(future, Duration.Inf)
  }

  println(tacticalReport("Jazz", "Bumblebee"))
  println(tacticalReport("Bumblebee", "Hot Rod"))
  println(tacticalReport("Jazz", "Ironhide"))

}
