package com.peknight.demo.squants

import squants.energy.MegawattHours
import squants.market.USD
import squants.space.UsGallons
import squants.{Dozen, Each}

object PriceApp extends App:
  val threeForADollar = USD(1) / Each(3)
  println(s"threeForADollar=$threeForADollar")
  val energyPrice = USD(102.20) / MegawattHours(1)
  println(s"energyPrice=$energyPrice")
  val milkPrice = USD(4) / UsGallons(1)
  println(s"milkPrice=$milkPrice")
  val costForABunch = threeForADollar * Dozen(10)
  println(s"costForABunch=$costForABunch")
  val energyCost = energyPrice * MegawattHours(4)
  println(s"energyCost=$energyCost")
  val milkQuota = USD(20) / milkPrice
  println(s"milkQuota=$milkQuota")

  val money = USD(123.456)
  val s1 = money.toString
  println(s"s1=$s1")
  val s2 = money.toFormattedString
  println(s"s2=$s2")


