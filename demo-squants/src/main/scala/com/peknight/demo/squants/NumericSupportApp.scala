package com.peknight.demo.squants

import squants.market.MoneyConversions.*
import squants.market.{MoneyContext, USD, defaultMoneyContext}
import squants.mass.MassConversions.MassNumeric
import squants.mass.{Grams, Kilograms}

object NumericSupportApp extends App:
  val sum = List(Kilograms(100), Grams(34510)).sum
  println(s"sum=$sum")
  given moneyContext: MoneyContext = defaultMoneyContext
  given moneyNum: MoneyNumeric = new MoneyNumeric()
  val sumUsd = List(USD(100), USD(10)).sum
  println(s"sumUsd=$sumUsd")

