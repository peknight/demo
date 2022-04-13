package com.peknight.demo.fs2.concurrencyprimitives

sealed trait Event

object Event {
  case class Text(value: String) extends Event
  case object Quit extends Event
}
