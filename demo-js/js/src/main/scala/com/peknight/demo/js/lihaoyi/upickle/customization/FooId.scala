package com.peknight.demo.js.lihaoyi.upickle.customization

import upickle.default.{ReadWriter, readwriter}

case class FooId(x: Int)
object FooId:
  given ReadWriter[FooId] = readwriter[Int].bimap[FooId](_.x, FooId(_))
