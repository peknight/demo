package com.peknight.demo.js.lihaoyi.handson.webpage

import org.scalajs.dom
import org.scalajs.dom.html
import scalatags.JsDom.all.*

import scala.scalajs.js.annotation.JSExportTopLevel

object CapitalBox:
  @JSExportTopLevel("capitalBox")
  def capitalBox(target: html.Div): Unit =
    val box = input(`type` := "text", placeholder := "Type here!").render
    val output = span.render
    box.onkeyup = (_: dom.Event) => output.textContent = box.value.toUpperCase
    target.appendChild(
      div(
        h1("Capital Box!"),
        p("Type here and have it capitalized!"),
        div(box),
        div(output)
      ).render
    )

