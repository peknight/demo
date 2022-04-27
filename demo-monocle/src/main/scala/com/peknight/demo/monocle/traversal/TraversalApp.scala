package com.peknight.demo.monocle.traversal

import alleycats.std.map.*
import cats.Applicative
import cats.syntax.applicative.*
import cats.syntax.traverse.*
import monocle.Traversal

object TraversalApp extends App:
  val xs = List(1, 2, 3, 4, 5)
  val eachL = Traversal.fromTraverse[List, Int]
  println(eachL.replace(0)(xs))
  println(eachL.modify(_ + 1)(xs))

  println(eachL.getAll(xs))
  println(eachL.headOption(xs))
  println(eachL.find(_ > 3)(xs))
  println(eachL.all(_ % 2 == 0)(xs))

  val points = Traversal.apply2[Point, Int](_.x, _.y)((x, y, p) => p.copy(x = x, y = y))

  println(points.replace(5)(Point("buttom-left", 0, 0)))

  def filterKey[K, V](predicate: K => Boolean): Traversal[Map[K, V], V] =
    new Traversal[Map[K, V], V]:
      def modifyA[F[_]: Applicative](f: V => F[V])(s: Map[K, V]): F[Map[K, V]] =
        s.map { case (k, v) => k -> (if predicate(k) then f(v) else v.pure[F]) }.sequence
  end filterKey

  val m = Map(1 -> "one", 2 -> "two", 3 -> "three", 4 -> "Four")

  val filterEven = filterKey[Int, String](_ % 2 == 0)

  println(filterEven.modify(_.toUpperCase)(m))
