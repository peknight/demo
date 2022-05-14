package com.peknight.demo.js.lihaoyi.upickle.basics

import upickle.default.{ReadWriter, macroRW}

case class Foo(i: Int)

object Foo:
  given ReadWriter[Foo] = macroRW
