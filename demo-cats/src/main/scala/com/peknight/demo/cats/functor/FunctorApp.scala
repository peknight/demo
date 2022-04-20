package com.peknight.demo.cats.functor

import cats.Functor
import cats.syntax.functor.*
import com.peknight.demo.cats.functor.FunctorInstances.{*, given}
import com.peknight.demo.cats.functor.Tree.Leaf

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.*
import scala.concurrent.{Await, Future}

object FunctorApp extends App:
  println(List(1, 2, 3).map(_ + 1))
  println(List(1, 2, 3).map(_ + 1).map(_ * 2).map(n => s"$n!"))

  val future: Future[String] = Future(123).map(_ + 1).map(_ * 2).map(n => s"$n!")
  println(Await.result(future, 1.second))

  import scala.util.Random

  val future1 =
    // Initialize Random with a fixed seed:
    val r = new Random(0L)
    // nextInt has the side-effect of moving to
    // the next random number in the sequence:
    val x = Future(r.nextInt())
    for
      a <- x
      b <- x
    yield (a, b)

  val future2 =
    val r = new Random(0L)
    for
      a <- Future(r.nextInt())
      b <- Future(r.nextInt())
    yield (a, b)

  val result1 = Await.result(future1, 1.second)
  println(result1)
  val result2 = Await.result(future2, 1.second)
  println(result2)


  val func1: Int => Double = (x: Int) => x.toDouble
  val func2: Double => Double = (y: Double) => y * 2

  println((func1 map func2)(1))
  println((func1 andThen func2)(1))
  println(func2(func1(1)))

  val func = ((x: Int) => x.toDouble).map(_ + 1).map(_ * 2).map(x => s"$x!")
  println(func(123))

  func1.map(func2)

  val list1 = List(1, 2, 3)
  val list2 = Functor[List].map(list1)(_ * 2)
  println(list2)

  val option1 = Option(123)
  val option2 = Functor[Option].map(option1)(_.toString)
  println(option2)

  val func3 = (x: Int) => x + 1
  val liftedFunc3 = Functor[Option].lift(func3)
  println(liftedFunc3(Option(1)))

  println(Functor[List].as(list1, "As"))

  val func4 = (a: Int) => a + 1
  val func5 = (a: Int) => a * 2
  val func6 = (a: Int) => s"$a!"
  val func7 = func4.map(func5).map(func6)
  println(func7(123))

  def doMath[F[_]](start: F[Int])(using functor: Functor[F]): F[Int] = start.map(n => n + 1 * 2)

  println(doMath(Option(20)))
  println(doMath(List(1, 2, 3)))

  val box = Box[Int](123)
  println(box.map(value => value + 1))
  println(List(1, 2, 3).as("As"))
  Future(123).map(_.toString())

  println(Functor[Future])

  println(Tree.branch(Leaf(10), Leaf(20)).map(_ * 2))

  type F[A] = Int => A
  val functor = Functor[F]

  val either: Either[String, Int] = Right(123)
  println(either.map(_ + 1))
