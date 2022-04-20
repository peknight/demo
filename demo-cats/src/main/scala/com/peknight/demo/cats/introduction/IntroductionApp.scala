package com.peknight.demo.cats.introduction

import com.peknight.demo.cats.introduction.JsonSyntax.*
import com.peknight.demo.cats.introduction.JsonWriterInstances.given
import com.peknight.demo.cats.introduction.PrintableInstances.given
import com.peknight.demo.cats.introduction.PrintableSyntax.*

object IntroductionApp extends App:
  println(Json.toJson(Person("Dave", "dave@example.com")))
  println(Person("Dave", "dave@example.com").toJson)

  println(summon[JsonWriter[String]])

  println(Json.toJson("A string!"))

  println(Json.toJson(Option("A string")))

  val cat = Cat("Java", 12, "Yellow")
  Printable.print(cat)
  cat.print
