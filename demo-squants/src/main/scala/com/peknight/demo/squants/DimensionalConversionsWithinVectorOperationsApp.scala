package com.peknight.demo.squants

import squants.motion.Velocity
import squants.space.{Area, Kilometers, Length, Meters}
import squants.time.Seconds
import squants.{DoubleVector, QuantityVector}

object DimensionalConversionsWithinVectorOperationsApp extends App:
  val vectorLength1 = QuantityVector(Kilometers(1.2), Kilometers(4.3), Kilometers(2.3))
  val vectorArea = vectorLength1.map[Area](_ * Kilometers(2))
  println(s"vectorArea=$vectorArea")
  val vectorVelocity = vectorLength1.map[Velocity](_ / Seconds(1))
  println(s"vectorVelocity=$vectorVelocity")
  val vectorDouble = DoubleVector(1.2, 4.3, 2.3)
  val vectorLength2 = vectorDouble.map[Length](Kilometers(_))
  println(s"vectorLength2=$vectorLength2")
  val vectorMetersNum = vectorLength1.to(Meters)
  println(s"vectorMetersNum=$vectorMetersNum")
  val vectorMeters = vectorLength1.in(Meters)
  println(s"vectorMeters=$vectorMeters")

