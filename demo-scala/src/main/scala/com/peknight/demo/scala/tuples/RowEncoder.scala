package com.peknight.demo.scala.tuples

trait RowEncoder[A]:
  def encodeRow(a: A): Row
