package com.peknight.demo.squants

import squants.energy.*

object DimensionalTypeSafetyApp extends App:

  val load1: Power = Kilowatts(12)
  println(s"load1=$load1")
  val load2: Power = Megawatts(0.023)
  println(s"load2=$load2")
  val sum1 = load1 + load2
  println(s"sum1=$sum1")

  given CanEqual[Power, Power] = CanEqual.derived
  println(s"sum1 == 35 kW: ${sum1 == Kilowatts(35)}")
  // comparisons automatically convert scale
  println(s"sum1 == 0.035 kW: ${sum1 == Megawatts(0.035)}")

  val load3: Power = Kilowatts(1.2)
  println(s"load3=$load3")

  val energy1: Energy = KilowattHours(23.0)
  println(s"energy1=$energy1")

  // val sum2 = load3 + energy1


