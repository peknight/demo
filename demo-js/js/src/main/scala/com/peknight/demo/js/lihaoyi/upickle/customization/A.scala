package com.peknight.demo.js.lihaoyi.upickle.customization

import upickle.default.{ReadWriter, macroRW}
import upickle.implicits.key

sealed trait A
object A:
  given ReadWriter[A] = ReadWriter.merge(B.rw, macroRW[C.type])

  @key("Bee") case class B(i: Int) extends A
  object B:
    given rw: ReadWriter[B] = macroRW

  case object C extends A
