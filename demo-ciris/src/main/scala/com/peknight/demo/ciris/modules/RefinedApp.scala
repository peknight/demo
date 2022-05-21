package com.peknight.demo.ciris.modules

import cats.implicits.*
import ciris.ConfigDecoder
import ciris.refined.*
import eu.timepit.refined.types.numeric.PosInt

object RefinedApp:

  val posIntConfigDecoder: ConfigDecoder[String, PosInt] = ConfigDecoder[String, PosInt]

