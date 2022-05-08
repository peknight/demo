package com.peknight.demo.js.lihaoyi.handson.webpage

import io.circe.Decoder

object OpenWeatherApi:
  case class Result(list: List[Info])
  case class Info(name: String, main: Main, sys: Sys, weather: List[Weather])
  case class Main(temp: Double, tempMin: Double, tempMax: Double, humidity: Double)
  given mainDecoder: Decoder[Main] = Decoder.forProduct4("temp", "temp_min", "temp_max", "humidity")(Main.apply)
  case class Sys(country: String)
  case class Weather(main: String)
