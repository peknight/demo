package com.peknight.demo.js.lihaoyi.upickle.basics

import upickle.default.{ReadWriter, macroRW}

case class Bar(name: String, foos: Seq[Foo])

object Bar:
  given ReadWriter[Bar] = macroRW


