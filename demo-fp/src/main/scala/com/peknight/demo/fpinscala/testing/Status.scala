package com.peknight.demo.fpinscala.testing

sealed trait Status derives CanEqual
object Status:
  case object Exhausted extends Status
  case object Proven extends Status
  case object Unfalsified extends Status
