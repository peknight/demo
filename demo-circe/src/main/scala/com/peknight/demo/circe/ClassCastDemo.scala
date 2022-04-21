package com.peknight.demo.circe

import io.circe.Codec
import io.circe.parser.decode

object ClassCastDemo extends App:
  // JsString转int测试
  case class Rua(a: String, b: Int) derives Codec.AsObject
  println(decode[Rua]("""{"a": "abc", "b":"3"}"""))
