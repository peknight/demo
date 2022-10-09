package com.peknight.demo.squants

import squants.energy.{Kilowatts, Power}
import squants.time.{Days, Hours}

object DimensionallyCorrectTypeConversionsApp extends App:
  val ratio = Days(1) / Hours(3)
  println(s"ratio=$ratio")

  val load = Kilowatts(1.2)
  val time = Hours(2)
  val energyUsed = load * time
  println(s"energyUsed=$energyUsed")

  val aveLoad: Power = energyUsed / time
  println(s"avgLoad=$aveLoad")

