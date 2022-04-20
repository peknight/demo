package com.peknight.demo.cats.monad

import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.{Id, Monad}

object IdApp extends App:

  val strId = "Dave": Id[String]
  val intId = 123: Id[Int]
  val listId = List(1, 2, 3): Id[List[Int]]

  val a = Monad[Id].pure(3)
  val b = Monad[Id].flatMap(a)(_ * 1)

  val addAns =
    for
      x <- a
      y <- b
    yield x + y

  println(addAns)
