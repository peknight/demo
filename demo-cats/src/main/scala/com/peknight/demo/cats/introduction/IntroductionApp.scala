package com.peknight.demo.cats.introduction

object IntroductionApp extends App {
  import JsonWriterInstances._
  println(Json.toJson(Person("Dave", "dave@example.com")))
  import JsonSyntax._
  println(Person("Dave", "dave@example.com").toJson)

  println(implicitly[JsonWriter[String]])

  println(Json.toJson("A string!"))

  println(Json.toJson(Option("A string")))

  val cat = Cat("Java", 12, "Yellow")
  import PrintableInstances._
  Printable.print(cat)
  import PrintableSyntax._
  cat.print
}
