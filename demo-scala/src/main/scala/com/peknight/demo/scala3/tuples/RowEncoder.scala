package com.peknight.demo.scala3.tuples

trait RowEncoder[A]:
  def encodeRow(a: A): Row
