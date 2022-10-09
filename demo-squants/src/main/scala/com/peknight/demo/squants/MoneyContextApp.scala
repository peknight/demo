package com.peknight.demo.squants

import squants.energy.MegawattHours
import squants.market.*

object MoneyContextApp extends App:
  val exchangeRate = List(USD / CAD(1.05), USD / MXN(12.45), USD / JPY(100))
  given moneyContext: MoneyContext = defaultMoneyContext withExchangeRates exchangeRate
  val energyPrice = USD(102.20) / MegawattHours(1)
  // 350 in the default Cur
  // 在defaultMoneyContext中 default Cur是美元
  val someMoney = Money(350)
  val usdMoney: Money = someMoney in USD
  println(s"usdMoney=$usdMoney")

  val usdBigDecimal: BigDecimal = someMoney to USD
  println(s"usdBigDecimal=$usdBigDecimal")

  val yenCost: Money = (energyPrice * MegawattHours(5)) in JPY
  println(s"yenCost=$yenCost")

  val northAmericanSales: Money = (CAD(275) + USD(350) + MXN(290)) in USD
  println(s"northAmericanSales=$northAmericanSales")
