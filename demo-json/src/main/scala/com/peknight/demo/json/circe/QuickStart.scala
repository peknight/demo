package com.peknight.demo.json.circe

import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

object QuickStart extends App {

  sealed trait Foo
  case class Bar(xs: Vector[String]) extends Foo
  case class Qux(i: Int, d: Option[Double]) extends Foo

  val foo: Foo = Qux(13, Some(14.0))

  val json = foo.asJson.noSpaces
  println(json)

  val decodedFoo = decode[Foo](json)
  println(decodedFoo)
}
