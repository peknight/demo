package com.peknight.demo.ciris.modules

import ciris.ConfigDecoder
import ciris.squants.*
import squants.market.Money

object SquantsApp:
  val moneyConfigDecoder: ConfigDecoder[String, Money] = ConfigDecoder[String, Money]


