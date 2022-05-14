package com.peknight.demo.js.lihaoyi.upickle.customization

import upickle.default.{ReadWriter, readwriter}

class CustomThing2(val i: Int, val s: String):
  override def toString: String = s"CustomThing2($i, $s)"
object CustomThing2:
  given ReadWriter[CustomThing2] = readwriter[String].bimap[CustomThing2](
    x => s"${x.i} ${x.s}",
    str =>
      val Array(i, s) = str.split(" ", 2)
      new CustomThing2(i.toInt, s)
  )

