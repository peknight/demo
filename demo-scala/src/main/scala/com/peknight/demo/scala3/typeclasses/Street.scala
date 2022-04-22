package com.peknight.demo.scala3.typeclasses

case class Street(value: String)
object Street:
  given streetToString: Conversion[Street, String] = _.value
