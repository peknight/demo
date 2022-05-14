package com.peknight.demo.js.lihaoyi.upickle.customization

import upickle.default.*

case class Bar(i: Int, s: String)
object Bar:
  given ReadWriter[Bar] = readwriter[ujson.Value].bimap[Bar](
    x => ujson.Arr(x.s, x.i),
    json => new Bar(json(1).num.toInt, json(0).str)
  )
