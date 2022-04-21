package com.peknight.demo.circe

import io.circe.generic.auto.exportDecoder
import io.circe.parser.decode

object ClassCastDemo extends App {

  // JsString转int测试
  case class Rua(a: String, b: Int)
  println(decode[Rua]("""{"a": "abc", "b":"3"}"""))
}
