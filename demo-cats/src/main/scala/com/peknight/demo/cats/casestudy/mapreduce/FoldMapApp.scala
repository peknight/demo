package com.peknight.demo.cats.casestudy.mapreduce

import cats.Monoid
import cats.syntax.foldable._
import cats.syntax.semigroup._
import cats.syntax.traverse._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object FoldMapApp extends App {
  def foldMap[A, B: Monoid](values: Vector[A])(func: A => B): B = {
//    values.foldLeft(Monoid[B].empty)((b, a) => func(a) |+| b)
    values.map(func).foldLeft(Monoid[B].empty)(_ |+| _)
  }

  def parallelFoldMap[A, B: Monoid](values: Vector[A])(func: A => B): Future[B] = {
    val numCores = Runtime.getRuntime.availableProcessors()
    val groupSize = (1.0 * values.size / numCores).ceil.toInt
//    val groups: Iterator[Vector[A]] = values.grouped(groupSize)
//    val futures: Iterator[Future[B]] = groups map { group =>
//      Future {
////        group.foldLeft(Monoid[B].empty)(_ |+| func(_))
//        foldMap(group)(func)
//      }
//    }
//    Future.sequence(futures) map { iterable =>
//      iterable.foldLeft(Monoid[B].empty)(_ |+| _)
//    }
    values
      .grouped(groupSize)
      .toVector
      .traverse(group => Future(group.foldMap(func)))
      .map(_.combineAll)
  }

  println(foldMap(Vector(1, 2, 3))(identity))

  println(foldMap(Vector(1, 2, 3))(_.toString + "! "))

  println(foldMap("Hello world!".toVector)(_.toString.toUpperCase))

  val result: Future[Int] = parallelFoldMap((1 to 1000000).toVector)(identity)

  println(Await.result(result, 1.second))
}
