package com.peknight.demo.squants

import squants.energy.Energy
import squants.energy.EnergyConversions.*
import squants.energy.PowerConversions.*
import squants.market.MoneyConversions.*
import squants.market.{Money, MoneyContext, Price, defaultMoneyContext}
import squants.mass.MassConversions.*
import squants.mass.{Density, Mass}
import squants.motion.AccelerationConversions.*
import squants.motion.{Acceleration, Velocity, VolumeFlow}
import squants.space.LengthConversions.*
import squants.space.VolumeConversions.*
import squants.time.Time
import squants.time.TimeConversions.*

object DimensionalAnalysisApp extends App:
  given moneyContext: MoneyContext = defaultMoneyContext
  val energyPrice: Price[Energy] = 45.25.money / megawattHour
  println(s"energyPrice=$energyPrice")
  val energyUsage: Energy = 345.kilowatts * 5.4.hours
  println(s"energyUsage=$energyUsage")
  val energyCost: Money = energyPrice * energyUsage
  println(s"energyCost=$energyCost")
  val dodgeViper: Acceleration = 60.miles / hour / 3.9.seconds
  println(s"dodgeViper=$dodgeViper")
  val speedAfter5Seconds: Velocity = dodgeViper * 5.seconds
  println(s"speedAfter5Seconds=$speedAfter5Seconds")
  val timeTo100MPH: Time = 100.miles / hour / dodgeViper
  println(s"timeTo100MPH=$timeTo100MPH")
  val density: Density = 1200.kilograms / cubicMeter
  println(s"density=$density")
  val volFlowRate: VolumeFlow = 10.gallons / minute
  println(s"volFlowRate=$volFlowRate")
  val flowTime: Time = 30.minutes
  println(s"flowTime=$flowTime")
  val totalMassFlow: Mass = volFlowRate * flowTime * density
  println(s"totalMassFlow=$totalMassFlow")

