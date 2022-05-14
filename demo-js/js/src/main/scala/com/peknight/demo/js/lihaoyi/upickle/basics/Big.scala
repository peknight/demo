package com.peknight.demo.js.lihaoyi.upickle.basics

import upickle.default.{ReadWriter, macroRW}

case class Big(i: Int, b: Boolean, str: String, c: Char, t: Thing)
object Big:
  given ReadWriter[Big] = macroRW

