package com.peknight.demo.js.lihaoyi.upickle.basics

import upickle.default.{ReadWriter, macroRW}

case class FooDefault(i: Int = 10, s: String = "lol")
object FooDefault:
  given ReadWriter[FooDefault] = macroRW
