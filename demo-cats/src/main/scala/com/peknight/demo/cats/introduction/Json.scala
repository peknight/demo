package com.peknight.demo.cats.introduction

sealed trait Json

object Json {
  final case class JsObject(get: Map[String, Json]) extends Json
  final case class JsString(get: String) extends Json
  final case class JsNumber(get: Double) extends Json
  final case object JsNull extends Json

  def toJson[A](value: A)(implicit w: JsonWriter[A]): Json = w.write(value)
}

