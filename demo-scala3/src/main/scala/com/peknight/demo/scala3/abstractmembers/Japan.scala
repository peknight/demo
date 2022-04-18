package com.peknight.demo.scala3.abstractmembers

object Japan extends CurrencyZone:
  abstract class Yen extends AbstractCurrency:
    def designation: String = "JPY"

  type Currency = Yen
  def make(yen: Long) =
    new Yen:
      val amount = yen

  val Yen = make(1)
  val CurrencyUnit = Yen
