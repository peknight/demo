package com.peknight.demo.js.dom

import org.scalajs.dom
import org.scalajs.dom.html

import scala.scalajs.js.annotation.JSExportTopLevel

object NodeAppendChildDemo:

  @JSExportTopLevel("nodeAppendChildDemo")
  def nodeAppendChildDemo(div: html.Div) =
    val child = dom.document.createElement("div")
    child.textContent = "Hi from Scala-js-dom: By nodeAppendChildDemo"
    div.appendChild(child)

