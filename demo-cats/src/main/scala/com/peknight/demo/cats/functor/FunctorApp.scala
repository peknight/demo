package com.peknight.demo.cats.functor

object FunctorApp extends App {
  println(List(1, 2, 3).map(_ + 1))
  println(List(1, 2, 3).map(_ + 1).map(_ * 2).map(n => s"${n}!"))

  import scala.concurrent.{Future, Await}
  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.duration._

  val future: Future[String] = Future(123).map(_ + 1).map(_ * 2).map(n => s"${n}!")
  println(Await.result(future, 1.second))

  import scala.util.Random

  val future1 = {
    // Initialize Random with a fixed seed:
    val r = new Random(0L)

    // nextInt has the side-effect of moving to
    // the next random number in the sequence:
    val x = Future(r.nextInt())

    for {
      a <- x
      b <- x
    } yield (a, b)
  }

  val future2 = {
    val r = new Random(0L)

    for {
      a <- Future(r.nextInt())
      b <- Future(r.nextInt())
    } yield (a, b)
  }

  val result1 = Await.result(future1, 1.second)
  println(result1)
  val result2 = Await.result(future2, 1.second)
  println(result2)

  import cats.syntax.functor._

  val func1: Int => Double = (x: Int) => x.toDouble
  val func2: Double => Double = (y: Double) => y * 2

  println((func1 map func2)(1))
  println((func1 andThen func2)(1))
  println(func2(func1(1)))

  val func = ((x: Int) => x.toDouble).map(_ + 1).map(_ * 2).map(x => s"${x}!")
  println(func(123))

  func1.map(func2)
}
