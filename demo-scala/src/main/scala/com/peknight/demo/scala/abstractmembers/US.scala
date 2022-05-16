package com.peknight.demo.scala.abstractmembers

object US extends CurrencyZone:
  abstract class Dollar extends AbstractCurrency:
    def designation = "USD"

  type Currency = Dollar
  def make(x: Long) =
    new Dollar:
      val amount = x

  val Cent = make(1)
  val Dollar = make(100)
  val CurrencyUnit = Dollar


