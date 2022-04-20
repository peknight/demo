package com.peknight.demo.cats.introduction

import com.peknight.demo.cats.introduction.Json.{JsNull, JsObject, JsString}

object JsonWriterInstances:

  given JsonWriter[String] with
    def write(value: String): Json = JsString(value)

  given JsonWriter[Person] with
    def write(value: Person): Json = JsObject(Map(
      "name" -> JsString(value.name),
      "email" -> JsString(value.email)
    ))

  given optionWriter[A](using writer: JsonWriter[A]): JsonWriter[Option[A]] with
    def write(option: Option[A]): Json = option match
      case Some(aValue) => writer.write(aValue)
      case None => JsNull