package com.peknight.demo.squants

import squants.energy.EnergyConversions.*
import squants.energy.PowerConversions.*
import squants.energy.PowerRampConversions.*
import squants.energy.{Energy, Power, PowerRamp}
import squants.market.MoneyConversions.*
import squants.market.Price
import squants.time.Time
import squants.time.TimeConversions.*

object DomainModelingApp extends App:
  // case class Generator(
  //   id: String,
  //   maxLoadKW: Double,
  //   rampRateKWph: Double,
  //   operatingCostPerMWh: Double,
  //   currency: String,
  //   maintenanceTimeHours: Double
  // )
  // val gen1 = Generator("Gen1", 5000, 7500, 75.4, "USD", 1.5)
  // val gen2 = Generator("Gen2", 100, 250, 2944.5, "JPY", 0.5)

  case class Generator(id: String, maxLoad: Power, rampRate: PowerRamp, operatingCost: Price[Energy],
                       maintenanceTime: Time)

  val gen1 = Generator("Gen1", 5.MW, 7.5.MW/hour, 75.4.USD/MWh, 1.5.hours)
  val gen2 = Generator("Gen2", 100.kW, 250.kWph, 2944.5.JPY/MWh, 30.minutes)
