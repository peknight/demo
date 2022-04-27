package com.peknight.demo.monocle.lens

import cats.syntax.functor.*
import com.peknight.demo.monocle.overview.Address
import monocle.Lens
import monocle.macros.GenLens

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object LensApp extends App:

  val streetNumber = Lens[Address, Int](_.streetNumber)(n => a => a.copy(streetNumber = n))
  val streetNumberViaMacro = GenLens[Address](_.streetNumber)

  val address = Address(10, "High Street")
  println(streetNumber.get(address))
  println(streetNumber.replace(5)(address))

  println(streetNumber.modify(_ + 1)(address))
  // equivalent to
  val n = streetNumber.get(address)
  println(streetNumber.replace(n + 1)(address))

  def neighbors(n: Int): List[Int] =
    if n > 0 then List(n - 1, n + 1) else List(n + 1)

  // 高级啊
  println(streetNumber.modifyF(neighbors)(address))

  println(streetNumber.modifyF(neighbors)(Address(135, "High Street")))

  def updateNumber(n: Int): Future[Int] = Future.successful(n + 1)

  println(Await.result(streetNumber.modifyF(updateNumber)(address), 1.second))

  val john = Person("John", 20, Address(10, "High Street"))
  val addressLen = GenLens[Person](_.address)
  println(addressLen.andThen(streetNumber).get(john))
  println(addressLen.andThen(streetNumber).replace(2)(john))

  // Other ways of Lens Composition

  println((GenLens[Person](_.name).replace("Mike") compose GenLens[Person](_.age).modify(_ + 1))(john))
  // macro based syntax:
  import monocle.syntax.all.*
  println(john.focus(_.name).replace("Mike").focus(_.age).modify(_ + 1))

  val c = GenLens[B](_.c)
  val b = GenLens[A](_.b)
  println(b.some.andThen(c).getOption(A(Some(B(1)))))
  println(b.some.andThen(c).getOption(A(None)))

  val age = GenLens[Person](_.age)
  println(GenLens[Person](_.address.streetName).replace("Iffley Road")(john))

  val p = Point(5, 3)
  // println(Point.x.get(p))
  // println(Point.y.replace(0)(p))

  val p2 = PrefixedPoint(5, 3)
  // println(PrefixedPoint._x.get(p))



