package com.peknight.demo.scala.typeclasses

case class Street(value: String)
object Street:
  given streetToString: Conversion[Street, String] = _.value
