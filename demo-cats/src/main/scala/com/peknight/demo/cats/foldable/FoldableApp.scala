package com.peknight.demo.cats.foldable

import cats.kernel.Monoid
import cats.syntax.semigroup._
import cats.syntax.foldable._
import cats.{Eval, Foldable}

object FoldableApp extends App {
  def show[A](list: List[A]): String =
    list.foldLeft("nil")((accum, item) => s"$item then $accum")

  println(show(Nil))
  println(show(List(1, 2, 3)))

  println(List(1, 2, 3).foldLeft(0)(_ + _))
  println(List(1, 2, 3).foldRight(0)(_ + _))

  println(List(1, 2, 3).foldLeft(0)(_ - _))
  println(List(1, 2, 3).foldRight(0)(_ - _))

  println(List(1, 2, 3).foldLeft(List.empty[Int])((accum, ele) => ele :: accum))
  println(List(1, 2, 3).foldRight(List.empty[Int])(_ :: _))

  def map[A, B](list: List[A])(func: A => B): List[B] = list.foldRight(List.empty[B])(func(_) :: _)

  def flatMap[A, B](list: List[A])(func: A => List[B]): List[B] = list.foldRight(List.empty[B])(func(_) ::: _)

  def filter[A](list: List[A])(func: A => Boolean): List[A] = list.foldRight(List.empty[A]) { (item, accum) =>
    if (func(item)) item :: accum else accum
  }

  def sumWithNumeric[A](list: List[A])(implicit numeric: Numeric[A]): A = list.foldRight(numeric.zero)(numeric.plus)

  def sum[A](list: List[A])(implicit m: Monoid[A]) = list.foldRight(m.empty)(_ |+| _)

  println(map(List(1, 2, 3))(i => s"$i!"))
  println(flatMap(List(1, 2, 3))(i => List(i - 1, i + 1)))
  println(filter(List(1, 2, 3))(_ % 2 == 0))
  println(sum(List(1, 2, 3)))
  println(sumWithNumeric(List(1, 2, 3)))

  val ints = List(1, 2, 3)
  println(Foldable[List].foldLeft(ints, 0)(_ + _))
  val maybeInt = Option(123)
  println(Foldable[Option].foldLeft(maybeInt, 10)(_ * _))

  def bigData = (1 to 100000).to(LazyList)

  println(bigData.foldRight(0L)(_ + _)) // 不会有StackOverflow问题

  val eval: Eval[Long] = Foldable[LazyList].foldRight(bigData, Eval.now(0L)) { (num, eval) =>
    eval.map(_ + num)
  }

  println(eval.value)

  println((1 to 100000).toList.foldRight(0L)(_ + _))
  println((1 to 100000).toVector.foldRight(0L)(_ + _))

  println(Foldable[Option].nonEmpty(Option(42)))
  println(Foldable[List].find(List(1, 2, 3))(_ % 2 == 0))

  println(Foldable[List].combineAll(List(1, 2, 3)))
  println(Foldable[Option].combineAll(Option.empty[Int]))

  println(Foldable[List].foldMap(List(1, 2, 3))(_.toString))

  println((Foldable[List] compose Foldable[Vector]).combineAll(List(Vector(1, 2, 3), Vector(4, 5, 6))))


  println(List(1, 2, 3).combineAll)
  println(List(1, 2, 3).foldMap(_.toString))

  println(List(1, 2, 3).foldLeft(0)(_ + _))

  def sum[F[_]: Foldable](values: F[Int]): Int = values.foldLeft(0)(_ + _)
}
