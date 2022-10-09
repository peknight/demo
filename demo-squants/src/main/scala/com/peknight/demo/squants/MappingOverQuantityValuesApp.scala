package com.peknight.demo.squants

import squants.energy.Kilowatts

object MappingOverQuantityValuesApp extends App:
  val load = Kilowatts(2.0)
  // 对于钱来说，使用mapAmount而不是map，mapAmount会保留BigDecimal的精度
  // q.map(f) == q.unit(f(q.to(q.unit)))
  val newLoad = load.map(v => v * 2 + 10)
  println(s"newLoad=$newLoad")


