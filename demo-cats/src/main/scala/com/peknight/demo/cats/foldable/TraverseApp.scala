package com.peknight.demo.cats.foldable

import cats.data.Validated
import cats.syntax.applicative._
import cats.syntax.apply._
import cats.syntax.traverse._
import cats.{Applicative, Traverse}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object TraverseApp extends App {
  val hostnames = List(
    "alpha.example.com",
    "beta.example.com",
    "gamma.demo.com"
  )

  def getUptime(hostname: String): Future[Int] = Future(hostname.length * 60)

  val allUptimesByFold: Future[List[Int]] = hostnames.foldLeft(Future(List.empty[Int])) {
    (accum, host) =>
      val uptime = getUptime(host)
      for {
        accum <- accum
        uptime <- uptime
      } yield accum :+ uptime
  }
  println(Await.result(allUptimesByFold, 1.second))

  val allUptimes: Future[List[Int]] = Future.traverse(hostnames)(getUptime)
  println(Await.result(allUptimes, 1.second))

  Future(List.empty[Int])
  List.empty[Int].pure[Future]

  def oldCombine(accum: Future[List[Int]], host: String): Future[List[Int]] = {
    val uptime = getUptime(host)
    for {
      accum <- accum
      uptime <- uptime
    } yield accum :+ uptime
  }

  def newCombine(accum: Future[List[Int]], host: String): Future[List[Int]] = {
    (accum, getUptime(host)).mapN(_ :+ _)
  }

  def listTraverse[F[_]: Applicative, A, B](list: List[A])(func: A => F[B]): F[List[B]] =
    list.foldLeft(List.empty[B].pure[F]) { (accum, item) =>
      (accum, func(item)).mapN(_ :+ _)
    }

  def listSequence[F[_]: Applicative, B](list: List[F[B]]): F[List[B]] = listTraverse(list)(identity)

  val totalUptimeByListTraverse = listTraverse(hostnames)(getUptime)
  println(Await.result(totalUptimeByListTraverse, 1.second))

  println(listSequence(List(Vector(1, 2), Vector(3, 4))))

  println(listSequence(List(Vector(1, 2), Vector(3, 4), Vector(5, 6))))

  def processOption(inputs: List[Int]) =
    listTraverse(inputs)(n => if (n % 2 == 0) Some(n) else None)

  println(processOption(List(2, 4, 6)))
  println(processOption(List(1, 2, 3)))

  type ErrorsOr[A] = Validated[List[String], A]

  def processValidated(inputs: List[Int]): ErrorsOr[List[Int]] = listTraverse(inputs) { n =>
    if (n % 2 == 0) {
      Validated.valid(n)
    } else {
      Validated.invalid(List(s"$n is not even"))
    }
  }

  println(processValidated(List(2, 4, 6)))
  println(processValidated(List(1, 2, 3)))

  val totalUptime: Future[List[Int]] = Traverse[List].traverse(hostnames)(getUptime)
  println(Await.result(totalUptime, 1.second))

  val numbers = List(Future(1), Future(2), Future(3))

  val numbers2: Future[List[Int]] = Traverse[List].sequence(numbers)

  println(Await.result(numbers2, 1.second))

  println(Await.result(hostnames.traverse(getUptime), 1.second))

  // 这里要把泛型带上，否则编译不通过
  println(Await.result(numbers.sequence[Future, Int], 1.second))
//  println(Await.result(numbers.sequence, 1.second)) // 编译失败
}
