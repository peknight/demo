package com.peknight.demo.js.lihaoyi.handson.webpage

import org.scalajs.dom
import org.scalajs.dom.ext.*
import org.scalajs.dom.html
import scalatags.JsDom.all.*

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object Weather1:

  @JSExportTopLevel("weather1")
  def weather1(target: html.Div): Unit =
    import js.Thenable.Implicits.*
    import scala.concurrent.ExecutionContext.Implicits.global
    // Define object OpenWeatherAppid { val appid = "#your appid here#" }
    val url = s"https://api.openweathermap.org/data/2.5/weather?q=Singapore&appid=${OpenWeatherAppid.appid}"
    for
      response <- dom.fetch(url)
      text <- response.text()
    do target.appendChild(format(text))

  def pretty(responseText: String): html.Pre =
    pre(js.JSON.stringify(js.JSON.parse(responseText), space = 4)).render

  def format(responseText: String): html.Div =
    val json = js.JSON.parse(responseText)
    val name = json.name.toString
    val weather = json.weather.pop().main.toString
    def celsius(kelvins: js.Dynamic) = (kelvins.asInstanceOf[Double] - 273.15).toInt
    val min = celsius(json.main.temp_min)
    val max = celsius(json.main.temp_max)
    val humid = json.main.humidity.toString
    div(
      b("Weather in Singapore:"),
      ul(
        li(b("Country "), name),
        li(b("Weather "), weather),
        li(b("Temp "), min, " - ", max),
        li(b("Humidity "), humid, "%")
      )
    ).render

