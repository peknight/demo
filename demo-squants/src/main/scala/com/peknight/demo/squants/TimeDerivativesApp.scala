package com.peknight.demo.squants

import squants.PowerRamp
import squants.energy.{Energy, Kilowatts, Power}
import squants.motion.{Acceleration, Velocity}
import squants.space.LengthConversions.*
import squants.space.{Kilometers, Length}
import squants.time.TimeConversions.*
import squants.time.{Hours, Seconds, Time}

object TimeDerivativesApp extends App:
  val distance: Length = Kilometers(100)
  println(s"distance=$distance")
  val time: Time = Hours(2)
  println(s"time=$time")
  val velocity: Velocity = distance / time
  println(s"velocity=$velocity")
  val acc: Acceleration = velocity / Seconds(1)
  println(s"acc=$acc")
  val gravity = 32.feet / second.squared
  println(s"gravity=$gravity")

  val power: Power = Kilowatts(100)
  println(s"power=$power")
  val energy: Energy = power * time
  println(s"energy=$energy")
  val ramp: PowerRamp = Kilowatts(50) / Hours(1)
  println(s"ramp=$ramp")

