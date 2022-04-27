package com.peknight.demo.monocle.prism

sealed trait Json

object Json:
  case object JNull extends Json
  case class JStr(v: String) extends Json
  case class JNum(v: Double) extends Json
  case class JObj(v: Map[String, Json]) extends Json
