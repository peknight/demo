package com.peknight.demo.scala3.abstractmembers

object Europe extends CurrencyZone:
  abstract class Euro extends AbstractCurrency:
    def designation = "EUR"

  type Currency = Euro
  def make(cents: Long) =
    new Euro:
      val amount = cents

  val Cent = make(1)
  val Euro = make(100)
  val CurrencyUnit = Euro
