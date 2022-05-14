package com.peknight.demo.js.lihaoyi.upickle.customization

import upickle.default.{ReadWriter, macroRW}

case class KeyBar(@upickle.implicits.key("hehehe") kekeke: Int)

object KeyBar:
  given ReadWriter[KeyBar] = macroRW
