package com.peknight.demo.js.lihaoyi.upickle.basics

import upickle.default.{ReadWriter, macroRW}

sealed trait IntOrTuple

object IntOrTuple:

  given ReadWriter[IntOrTuple] = ReadWriter.merge(IntThing.rw, TupleThing.rw)

  case class IntThing(i: Int) extends IntOrTuple
  object IntThing:
    given rw: ReadWriter[IntThing] = macroRW

  case class TupleThing(name: String, t: (Int, Int)) extends IntOrTuple
  object TupleThing:
    given rw: ReadWriter[TupleThing] = macroRW
