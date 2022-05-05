package com.peknight.demo.js.dom

import org.scalajs.dom
import org.scalajs.dom.html

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object DomFetchDemo:
  @JSExportTopLevel("domFetchDemo")
  def domFetchDemo(pre: html.Pre) =
    import js.Thenable.Implicits.*
    import scala.concurrent.ExecutionContext.Implicits.global
    val url = "https://www.boredapi.com/api/activity"
    val responseText =
      for
        response <- dom.fetch(url)
        text <- response.text()
      yield text
    for text <- responseText do pre.textContent = text

