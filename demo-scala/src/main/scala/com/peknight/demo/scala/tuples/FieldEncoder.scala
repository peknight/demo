package com.peknight.demo.scala.tuples

trait FieldEncoder[A]:
  def encodeField(a: A): String
