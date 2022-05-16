package com.peknight.demo.scala3.tuples

trait FieldEncoder[A]:
  def encodeField(a: A): String
