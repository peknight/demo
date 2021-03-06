package com.peknight.demo.cats.monad

import cats.Monad
import cats.syntax.applicative._

object ListApp extends App {
  val listFlatMap1 = for {
    x <- (1 to 3).toList
    y <- (4 to 5).toList
  } yield (x, y)

  println(listFlatMap1)

  val list1 = Monad[List].pure(3)
  println(list1)
  val list2 = Monad[List].flatMap(List(1, 2, 3))(a => List(a, a * 10))
  println(list2)
  val list3 = Monad[List].map(list2)(a => a + 123)
  println(list3)

  println(Monad[Vector].flatMap(Vector(1, 2, 3))(a => Vector(a, a * 10)))

  1.pure[List]
}
