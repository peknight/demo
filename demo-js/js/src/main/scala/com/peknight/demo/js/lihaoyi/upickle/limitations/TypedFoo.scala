package com.peknight.demo.js.lihaoyi.upickle.limitations

import upickle.default.*

sealed trait TypedFoo
object TypedFoo:
  given ReadWriter[TypedFoo] = ReadWriter.merge(macroRW[Bar], macroRW[Baz], macroRW[Quz])
  case class Bar(i: Int) extends TypedFoo
  case class Baz(s: String) extends TypedFoo
  case class Quz(b: Boolean) extends TypedFoo

