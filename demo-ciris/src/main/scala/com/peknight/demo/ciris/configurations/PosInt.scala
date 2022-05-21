package com.peknight.demo.ciris.configurations

import ciris.ConfigDecoder

sealed abstract case class PosInt(value: Int)

object PosInt:
  def apply(value: Int): Option[PosInt] = if value > 0 then Some(new PosInt(value) {}) else None

  given ConfigDecoder[String, PosInt] = ConfigDecoder[String, Int].mapOption("PosInt")(apply)
