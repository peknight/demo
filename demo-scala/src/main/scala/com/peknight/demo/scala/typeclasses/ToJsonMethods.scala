package com.peknight.demo.scala.typeclasses

object ToJsonMethods:
  extension [T](a: T)(using jser: JsonSerializer[T])
    def toJson: String = jser.serialize(a)
