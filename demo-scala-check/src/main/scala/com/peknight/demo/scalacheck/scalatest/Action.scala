package com.peknight.demo.scalacheck.scalatest

abstract class Action derives CanEqual
object Action:
  case object Start extends Action
  case object Click extends Action
  case class Enter(n: Int) extends Action
end Action