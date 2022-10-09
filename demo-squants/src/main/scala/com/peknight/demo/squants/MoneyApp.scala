package com.peknight.demo.squants

import squants.market.{BTC, JPY, USD, XAU}

object MoneyApp extends App:
  val tenBucks = USD(10)
  println(s"tenBucks=$tenBucks")
  val someYen = JPY(1200)
  println(s"someYen=$someYen")
  val goldStash = XAU(50)
  println(s"goldStash=$goldStash")
  val digitalCache = BTC(50)
  println(s"digitalCache=$digitalCache")

