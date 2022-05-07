package com.peknight.demo.js.lihaoyi.handson.webpage

import org.scalajs.dom.html
import scalatags.JsDom.all.*

import scala.scalajs.js.annotation.JSExportTopLevel

object HelloWorld1:

  @JSExportTopLevel("helloWorld1")
  def helloWorld1(target: html.Div): Unit =
    val (animalA, animalB) = ("fox", "dog")
    target.appendChild(div(
      h1("Hello World!"),
      p("The quick brown ", b(animalA), " jumps over the lazy ", i(animalB), ".")
    ).render)
