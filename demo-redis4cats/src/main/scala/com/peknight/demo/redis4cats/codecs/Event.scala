package com.peknight.demo.redis4cats.codecs

sealed trait Event
object Event:
  case class Ack(id: Long) extends Event
  case class Message(id: Long, payload: String) extends Event
  case object Unknown extends Event
