package com.peknight.demo.js.lihaoyi.upickle.basics

import upickle.default.{ReadWriter, macroRW}

case class Thing(myFieldA: Int, myFieldB: String)
object Thing:
  given ReadWriter[Thing] = macroRW
