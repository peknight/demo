package com.peknight.demo.squants

import squants.*
import squants.energy.EnergyConversions.*
import squants.energy.PowerConversions.*
import squants.energy.{Kilowatts, MegawattHours, Power}
import squants.information.InformationConversions.*
import squants.market.MoneyConversions.*
import squants.market.{Price, USD}
import squants.space.LengthConversions.*
import squants.time.Hours
import squants.time.TimeConversions.*

object NaturalLanguageDslApp extends App:
  val load = Kilowatts(100)
  val time = Hours(3.75)
  val money = USD(112.50)
  val price = Price(money, MegawattHours(1))
  val load1 = 100.kW
  val load2 = 100.megawatts
  val time1 = 3.hours + 45.minutes
  println(s"time1=$time1")
  val energyUsed = 100.kilowatts * (3.hours + 45.minutes)
  println(s"energyUsed=$energyUsed")
  val price1 = 112.50.USD / 1.megawattHours
  println(s"price1=$price1")
  val speed = 55.miles / 1.hours
  println(s"speed=$speed")

  val loadTry1 = Power("40 MW")
  println(s"loadTry1=$loadTry1")
  val loadTry2 = Power((40.5, "MW"))
  println(s"loadTry2=$loadTry2")

  val ramp = 100.kilowatts / hour
  println(s"ramp=$ramp")
  val speed2 = 100.kilometers / hour
  println(s"speed2=$speed2")
  val hi = 100.dollars / MWh
  println(s"hi=$hi")
  val low = 40.dollars / megawattHour
  println(s"low=$low")

  val load3 = 10.22 * 4.MW
  println(s"load3=$load3")
  val driveArrayCapacity = 12 * 600.gb
  println(s"driveArrayCapacity=$driveArrayCapacity")
  val freq = 60 / second
  println(s"freq=$freq")
  val freq2 = BigDecimal(36000000) / hour
  println(s"freq2=$freq2")

  val range1 = 1000.kW to 5000.kW
  println(s"range1=$range1")
  val range2 = 5000.kW plusOrMinus 1000.kW
  println(s"range2=$range2")
  val range3 = 5000.kW +- 1000.kW
  println(s"range3=$range3")

