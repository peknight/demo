package com.peknight.demo.frontend.heima.pink.webapis

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object DataSetScript:

  @JSExportTopLevel("dataSet")
  def dataSet(): Unit =
    val div = dom.document.querySelector("div").asInstanceOf[dom.HTMLElement]
    dom.console.log(div.getAttribute("getTime"))
    div.setAttribute("data-time", "20")
    dom.console.log(div.getAttribute("data-index"))
    dom.console.log(div.getAttribute("data-list-name"))
    dom.console.log(div.dataset)
    dom.console.log(div.dataset("index"))
    dom.console.log(div.dataset("listName"))

