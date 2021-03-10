package com.peknight.demo.cats.semigroupal

import cats.syntax.apply._
import cats.syntax.semigroup._
import cats.{Monoid, Semigroupal}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future} // for |+|

object ApplyApp extends App {
  println((Option(123), Option("abc")).tupled)
  println((Option(123), Option("abc"), Option(true)).tupled)

  println((Option("Garfield"), Option(1978), Option("Orange & black")).mapN(CatWithColor.apply))

  val add: (Int, Int) => Int = (a, b) => a + b

  val tupleToCat: (String, Int, List[String]) => Cat = Cat.apply _

  val catToTuple: Cat => (String, Int, List[String]) = cat => (cat.name, cat.yearOfBirth, cat.favoriteFoods)

  implicit val catMonoid: Monoid[Cat] = (
    Monoid[String], Monoid[Int], Monoid[List[String]]
    ).imapN(tupleToCat)(catToTuple)

  val garfield = Cat("Garfield", 1978, List("Lasagne"))
  val healthcliff = Cat("Heathcliff", 1988, List("Junk Food"))

  println(garfield |+| healthcliff)


  // Future

  val futurePair = Semigroupal[Future].product(Future("Hello"), Future(123))
  println(Await.result(futurePair, 1.second))

  val futureCat = (
    Future("Garfield"),
    Future(1978),
    Future(List("Lasagne"))
  ).mapN(Cat.apply)

  println(Await.result(futureCat, 1.second))


  // List

  println(Semigroupal[List].product(List(1, 2), List(3, 4)))
  println((List(1, 2), List(3, 4)).tupled)

}
