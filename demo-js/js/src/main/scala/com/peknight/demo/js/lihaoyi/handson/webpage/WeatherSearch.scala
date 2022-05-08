package com.peknight.demo.js.lihaoyi.handson.webpage

import com.peknight.demo.js.lihaoyi.handson.webpage.OpenWeatherApi.{*, given}
import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.*
import org.scalajs.dom
import org.scalajs.dom.html
import scalatags.JsDom.all.*

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object WeatherSearch:

  lazy val box = input(`type` := "text", placeholder := "Type here!").render
  lazy val output = div(height := "400px", overflowY := "scroll").render

  def fetchWeather(query: String) =
    import js.Thenable.Implicits.*
    import scala.concurrent.ExecutionContext.Implicits.global
    // Define object OpenWeatherAppid { val appid = "#your appid here#" }
    val searchUrl = s"http://api.openweathermap.org/data/2.5/find?type=like&mode=json&q=$query&appid=${OpenWeatherAppid.appid}"
    for
      response <- dom.fetch(searchUrl) if query == box.value
      text <- response.text()
    do decode[Result](text) match
      case Right(Result(list)) if list.nonEmpty =>
        output.innerHTML = ""
        showResults(list, query)
      case _ => output.innerHTML = "No Results"

  def showResults(infos: List[Info], query: String) =
    for info <- infos do
      val name = info.name
      val country = info.sys.country
      val weather = info.weather.headOption.map(_.main).getOrElse("")
      def celsius(kelvins: Double) = (kelvins - 273.15).toInt
      val min = celsius(info.main.tempMin)
      val max = celsius(info.main.tempMax)
      val humid = info.main.humidity.toInt.toString
      val (first, last) = info.name.splitAt(query.length)
      output.appendChild(
        div(
          b(span(first, backgroundColor := "yellow"), last, ", ", country),
          ul(
            li(b("Weather "), weather),
            li(b("Temp "), min, " - ", max),
            li(b("Humidity "), humid, "%")
          )
        ).render
      )

  @JSExportTopLevel("weatherSearch")
  def weatherSearch(target: html.Div): Unit =
    val searchButton = button().render
    searchButton.textContent = "GO!"
    searchButton.onclick = (e: dom.MouseEvent) =>
      output.innerHTML = "Loading..."
      fetchWeather(box.value)
    // box.onkeyup = (e: dom.Event) =>
    //   output.innerHTML = "Loading..."
    //   fetchWeather(box.value)

    target.appendChild(
      div(
        h1("Weather Search"),
        p(
          "Enter the name of a city to pull the ",
          "latest weather data from api.openweathermap.com!"
        ),
        p(box, searchButton),
        hr, output, hr
      ).render
    )

