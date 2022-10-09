package com.peknight.demo.squants

import squants.market.{CurrencyExchangeRate, JPY, Money, USD}

object FxSupportApp extends App:
  val rate1 = CurrencyExchangeRate(USD(1), JPY(100))
  println(rate1)
  val rate2 = USD / JPY(100)
  println(rate2)
  val rate3 = JPY(100) -> USD(1)
  println(rate3)
  val rate4 = JPY(100) toThe USD(1)
  println(rate4)
  val rate5 = CurrencyExchangeRate(JPY(100), USD(1))
  println(rate5)
  val rate6 = JPY / USD(0.01)
  println(rate6)
  val rate7 = USD(1) -> JPY(100)
  println(rate7)
  val rate8 = USD(1) toThe JPY(100)
  println(rate8)
  val someYen: Money = JPY(350)
  val someBucks: Money = USD(23.50)

  val dollarAmount: Money = rate1.convert(someYen)
  println(s"dollarAmount=$dollarAmount")
  val yenAmount: Money = rate1.convert(someBucks)
  println(s"yenAmount=$yenAmount")
  val dollarAmount2: Money = rate1 * someYen
  println(s"dollarAmount2=$dollarAmount2")
  val yenAmount2: Money = someBucks * rate1
  println(s"yenAmount2=$yenAmount2")

