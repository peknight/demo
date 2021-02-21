package com.peknight.demo.json.circe

import io.circe.generic.auto._
import io.circe.syntax._

object AutomaticDerivation extends App {

  case class Person(name: String)
  case class Greeting(salutation: String, person: Person, exclamationMarks: Int)

  println(Greeting("Hey", Person("Chris"), 3).asJson)
  // res0: io.circe.Json = JObject(
  //   object[salutation -> "Hey",person -> {
  //   "name" : "Chris"
  // },exclamationMarks -> 3]
  // )
}
