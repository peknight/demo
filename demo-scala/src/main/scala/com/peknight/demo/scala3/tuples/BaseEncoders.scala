package com.peknight.demo.scala3.tuples

object BaseEncoders:
  given FieldEncoder[Int] with
    def encodeField(x: Int) = x.toString

  given FieldEncoder[Boolean] with
    def encodeField(x: Boolean) = if x then "true" else "false"

  given FieldEncoder[String] with
    def encodeField(x: String) = x // Ideally, we should also escape commas and double quotes
end BaseEncoders

