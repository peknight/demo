package com.peknight.demo.js.lihaoyi.handson.webpage

import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.html
import scalatags.JsDom.all._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object WeatherSearch {
  lazy val box = input(
    `type` := "text",
    placeholder := "Type here!"
  ).render

  lazy val output = div(
    height := "400px",
    overflowY := "scroll"
  ).render

  def fetchWeather(query: String) = {
    // Define object OpenWeatherAppid { val appid = "#your appid here#" }
    val searchUrl = s"http://api.openweathermap.org/data/" +
      s"2.5/find?type=like&mode=json&q=${query}&appid=${OpenWeatherAppid.appid}"
    import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
    for {
      xhr <- Ajax.get(searchUrl) if query == box.value
    } yield js.JSON.parse(xhr.responseText).list match {
      case jsonlist: js.Array[js.Dynamic] @unchecked =>
        output.innerHTML = ""
        showResults(jsonlist, query)
      case _ =>
        output.innerHTML = "No Results"
    }
  }

  def showResults(jsonlist: js.Array[js.Dynamic], query: String) = {
    for (json <- jsonlist) {
      val name = json.name.toString
      val country = json.sys.country.toString
      val weather = json.weather.pop().main.toString

      val min = Weather.celsius(json.main.temp_min)
      val max = Weather.celsius(json.main.temp_max)
      val humid = json.main.humidity.toString
      val (first, last) = name.splitAt(query.length)

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
    }
  }

  @JSExportTopLevel("weatherSearch")
  def weatherSearch(target: html.Div): Unit = {
    val searchButton = button().render
    searchButton.textContent = "GO!"
    searchButton.onclick = (e: dom.MouseEvent) => {
      output.innerHTML = "Loading..."
      fetchWeather(box.value)
    }

//    box.onkeyup = (e: dom.Event) => {
//      output.innerHTML = "Loading..."
//      fetchWeather(box.value)
//    }

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
  }
}
