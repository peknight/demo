package com.peknight.demo.http4s.entityhandling

sealed trait Resp

object Resp:
  case class Audio(body: String) extends Resp
  case class Video(body: String) extends Resp

