package com.peknight.demo.squants

import squants.energy.EnergyConversions.megawattHour
import squants.energy.{Energy, MegawattHours, Megawatts, Power}
import squants.market.{MoneyContext, Price, USD}
import squants.radio.{Irradiance, WattsPerSquareMeter}
import squants.thermal.{Celsius, Fahrenheit, Temperature}

object AnticorruptionLayersApp extends App:
  trait ScadaService:
    // ScadaService returns meter load as Double representing Megawatts
    def getLoad(meterId: Long): Double
    // ScadaService.sendTempBias requires a Double representing Fahrenheit
    def sendTempBias(temp: Double): Unit
  end ScadaService

  val meterId = 0L

  class ScadaServiceAnticorruption(val service: ScadaService):
    def getLoad: Power = Megawatts(service.getLoad(meterId))
    def sendTempBias(temp: Temperature): Unit = service.sendTempBias(temp.to(Fahrenheit))
  end ScadaServiceAnticorruption

  trait WeatherService:
    def getTemperature: Double
    def getIrradiance: Double
  end WeatherService

  trait WeatherServiceAntiCorruption:
    val service: WeatherService
    def getTemperature: Temperature = Celsius(service.getTemperature)
    def getIrradiance: Irradiance = WattsPerSquareMeter(service.getIrradiance)
  end WeatherServiceAntiCorruption

  trait MarketService:
    // MarketService.getPrice returns a Double representing $/MegawattHour
    def getPrice: Double
    // MarketService.sendBid requires a Double representing $/MegawattHour
    // and another Double representing the max amount of energy in MegawattHours
    def sendBid(bid: Double, limit: Double): Unit
  end MarketService
  class MarketServiceAnticorruption(val service: MarketService)(using moneyContext: MoneyContext):
    def getPrice: Price[Energy] = (USD(service.getPrice) in moneyContext.defaultCurrency) / megawattHour
    def sendBid(bid: Price[Energy], limit: Energy) = service.sendBid((bid * megawattHour) to USD, limit to MegawattHours)
  end MarketServiceAnticorruption
