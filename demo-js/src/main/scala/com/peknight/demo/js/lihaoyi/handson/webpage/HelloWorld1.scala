package com.peknight.demo.js.lihaoyi.handson.webpage

import org.scalajs.dom.html

import scala.scalajs.js.annotation.JSExportTopLevel
import scalatags.JsDom.all._

object HelloWorld1 {
  @JSExportTopLevel("helloWorld1")
  def helloWorld1(target: html.Div): Unit = {
    val (animalA, animalB) = ("fox", "dog")
    target.appendChild(
      div(
        h1("Hello World!"),
        p(
          "The quick brown ", b(animalA),
          " jumps over the lazy ",
          i(animalB), "."
        )
      ).render
    )
  }
}
