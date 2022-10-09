package com.peknight.demo.squants

import squants.space.Length
import squants.{Quantity, UnitOfMeasure}

/**
 * Experimental内容，先跳过
 */
object UnitGroupsApp extends App:
  def mkConversionFactor[A <: Quantity[A]](uom: UnitOfMeasure[A]): Double =
    val one = uom(1)
    one.to(one.dimension.siUnit)

  def mkTuple[A <: Quantity[A]](uom: UnitOfMeasure[A]): (String, Double) =
    (uom.symbol, mkConversionFactor(uom))

