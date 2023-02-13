package com.peknight.demo.scalatest.async

sealed abstract class StringOp derives CanEqual
object StringOp:
  case object Clear extends StringOp
  case class Append(value: String) extends StringOp
  case object GetValue
end StringOp
