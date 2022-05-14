package com.peknight.demo.js.lihaoyi.upickle.customization

import upickle.default.*

case class Wrap(i: Int)
object Wrap:
  given ReadWriter[Wrap] = readwriter[Int].bimap[Wrap](_.i, Wrap(_))
