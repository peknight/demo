package com.peknight.demo.squants

import squants.energy.{Gigawatts, Kilowatts, Megawatts, Power}
import squants.mass.MassConversions.*
import squants.mass.{Kilograms, Pounds}
import squants.thermal.Fahrenheit
import squants.thermal.TemperatureConversions.*

object UnitConversionsApp extends App:
  val loadA: Power = Kilowatts(1200)
  val loadB: Power = Megawatts(1200)
  val loadC: Power = loadA in Megawatts
  val loadD: Power = loadA in Gigawatts
  println(s"loadC=$loadC")
  println(s"loadD=$loadD")
  val kw: Double = loadA to Kilowatts
  val mw: Double = loadA to Megawatts
  val gw: Double = loadA to Gigawatts
  println(s"kw=$kw")
  println(s"mw=$mw")
  println(s"gw=$gw")
  val kwStr: String = loadA toString Kilowatts
  val mwStr: String = loadA toString Megawatts
  val gwStr: String = loadA toString Gigawatts
  println(s"kwStr=$kwStr")
  println(s"mwStr=$mwStr")
  println(s"gwStr=$gwStr")

  val kwTup = loadA.toTuple
  val mwTup = loadA toTuple Megawatts
  val gwTup = loadA toTuple Gigawatts
  println(s"kwTup=$kwTup")
  println(s"mwTup=$mwTup")
  println(s"gwTup=$gwTup")

  println(1.kilograms to Pounds)
  println(kilogram / pound)
  println(2.1.pounds to Kilograms)
  println(2.1.pounds / kilogram)
  println(100.C to Fahrenheit)



