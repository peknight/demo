package com.peknight.demo.fs2.concurrencyprimitives

sealed trait Event derives CanEqual

object Event:
  case class Text(value: String) extends Event
  case object Quit extends Event
