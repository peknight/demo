package com.peknight.demo.fpinscala.monads

import com.peknight.demo.fpinscala.monads.Monad.{listMonad, stateMonad}
import com.peknight.demo.fpinscala.state.State
import com.peknight.demo.fpinscala.testing.Exhaustive.Gen

object MonadsApp extends App {
  val listFunctor = new Functor[List] {
    def map[A, B](as: List[A])(f: A => B): List[B] = as map f
  }

  case class Order(item: Item, quantity: Int)
  case class Item(name: String, price: Double)

  val genItem: Gen[Item] = for {
    name <- Gen.stringN(3)
    price <- Gen.uniform.map(_ * 10)
    quantity <- Gen.choose(1, 100)
  } yield Item(name, price)

  val genOrder: Gen[Order] = for {
    item <- genItem
    quantity <- Gen.choose(1, 100)
  } yield Order(item, quantity)

  // Exercise 11.14

  val listMonadJoinRes = listMonad.join(List(List(1, 2), List(3, 4)))
  println(listMonadJoinRes)

  val ns: List[List[List[Int]]] = List(List(List(1, 2), List(3, 4)), List(List(), List(5)))

  val nsFlattenRes = ns.flatten
  println(nsFlattenRes)

  val nsMapRes = ns.map(listMonad.join)
  println(nsMapRes)

  val nsJoinEquals = listMonad.join(nsFlattenRes) == listMonad.join(nsMapRes)
  println(nsJoinEquals)

  val F = stateMonad[Int]

  def zipWithIndex[A](as: List[A]): List[(Int, A)] = as.foldLeft(F.unit(List.empty[(Int, A)]))((acc, a) => for {
    xs <- acc
    n <- State.get
    _ <- State.set(n + 1)
  } yield (n, a) :: xs).run(0)._1.reverse

}

