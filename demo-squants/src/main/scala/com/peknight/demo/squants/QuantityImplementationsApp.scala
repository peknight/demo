package com.peknight.demo.squants

import squants.space.Feet
import squants.{Dimension, PrimaryUnit, Quantity, SiBaseUnit, SiUnit, UnitConverter, UnitOfMeasure}

object QuantityImplementationsApp extends App:
  class Length(val value: Double, val unit: LengthUnit) extends Quantity[Length]:
    def dimension: Dimension[Length] = Length
  object Length extends Dimension[Length]:
    def name: String = "Length"
    def units: Set[UnitOfMeasure[Length]] = Set(Meters, Yards)
    def primaryUnit: UnitOfMeasure[Length] with PrimaryUnit = Meters
    def siUnit: UnitOfMeasure[Length] with SiUnit = Meters
  trait LengthUnit extends UnitOfMeasure[Length] with UnitConverter:
    def apply[A](n: A)(using num: Numeric[A]) = new Length(num.toDouble(n), this)
  object Meters extends LengthUnit with PrimaryUnit with SiBaseUnit:
    val symbol: String = "m"
  object Yards extends LengthUnit:
    val conversionFactor: Double = Feet.conversionFactor * 3d
    val symbol: String = "yd"

  val len1: Length = Meters(4.3)
  val len2: Length = Yards(5)
  println(s"len1=$len1")
  println(s"len2=$len2")
