package com.peknight.demo.js.lihaoyi.handson.webpage

import org.scalajs.dom
import org.scalajs.dom.html
import scalatags.JsDom.all._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object Weather1 {
  @JSExportTopLevel("weather1")
  def weather1(target: html.Div): Unit = {
    import dom.ext._

    import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
    val url = s"http://api.openweathermap.org/data/2.5/weather?q=Singapore&appid=${OpenWeatherAppid.appid}"
    Ajax.get(url).map { case xhr => target.appendChild(
//      format(xhr.responseText)
      pretty(xhr.responseText)
    )}
  }

  def pretty(responseText: String): html.Pre = {
    pre(js.JSON.stringify(js.JSON.parse(responseText), space = 4)).render
  }

  def format(responseText: String): html.Div = {
    val json = js.JSON.parse(responseText)
    val name = json.name.toString
    val weather = json.weather.pop().main.toString
    val min = Weather.celsius(json.main.temp_min)
    val max = Weather.celsius(json.main.temp_max)
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
  }
}
