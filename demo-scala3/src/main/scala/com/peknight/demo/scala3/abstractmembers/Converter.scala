package com.peknight.demo.scala3.abstractmembers

object Converter:
  var exchangeRate = Map(
    "USD" -> Map("USD" -> 1.0, "EUR" -> 0.8498, "JPY" -> 1.047, "CHF" -> 0.9149),
    "EUR" -> Map("USD" -> 1.177, "EUR" -> 1.0, "JPY" -> 1.232, "CHF" -> 1.0765),
    "JPY" -> Map("USD" -> 0.9554, "EUR" -> 0.8121, "JPY" -> 1.0, "CHF" -> 0.8742),
    "CHF" -> Map("USD" -> 1.093, "EUR" -> 0.9289, "JPY" -> 1.144, "CHF" -> 1.0),
  )
