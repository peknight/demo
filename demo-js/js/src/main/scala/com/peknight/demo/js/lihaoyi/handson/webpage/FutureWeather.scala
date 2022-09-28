package com.peknight.demo.js.lihaoyi.handson.webpage

import cats.syntax.traverse.*
import com.peknight.demo.js.lihaoyi.handson.webpage.OpenWeatherApi.{*, given}
import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.*
import org.scalajs.dom
import org.scalajs.dom.{KeyCode, html}
import scalatags.JsDom.all.*

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.Thenable.Implicits.*
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("FutureWeather")
object FutureWeather:

  def url(city: String) = s"http://api.openweathermap.org/data/2.5/find?mode=json&q=$city&appid=${OpenWeatherAppid.appid}"

  def request(names: Seq[String]): Future[Seq[(String, Double)]] =
    val responses = for name <- names yield
      for
        response <- dom.fetch(url(name))
        text <- response.text()
      yield
        for
          result <- decode[Result](text).toOption
          info <- result.list.headOption
        yield (name, info.main.temp - 272.15)
    responses.sequence.map(_.collect {
      case Some(resp) => resp
    })

  def renderResults(output: html.Element, results: Seq[(String, Double)]) =
    output.innerHTML = ""
    output.appendChild(ul(for (name, temp) <- results yield li(b(name), "-", temp.toInt, "℃")).render)

  @JSExport
  def futureWeather(container: html.Div) =
    val myInput = input(value := "London,Singapore,Berlin,New York").render
    val output = div.render
    myInput.onkeyup = (e: dom.KeyboardEvent) => if e.keyCode == KeyCode.Enter then
      for results <- request(myInput.value.split(',').toIndexedSeq) do renderResults(output, results)
    container.appendChild(div(
      i("Press Enter in the box to fetch temperatures "),
      myInput,
      output
    ).render)

