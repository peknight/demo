package com.peknight.demo.squants

import squants.energy.{Kilowatts, Power, Watts}

object ApproximationsApp extends App:
  val load = Kilowatts(2.0)
  val reading = Kilowatts(1.9999)
  given tolerance: Power = Watts(.1)
  println(load =~ reading)
  println(load ≈ reading)
  println(load approx reading)
  // =~ ≈ ~= 都是approx，~=的优先级较低，可能需要加括号

