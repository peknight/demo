package com.peknight.demo.js.dom

import org.scalajs.dom
import org.scalajs.dom.html

import scala.scalajs.js.annotation.JSExportTopLevel

object DomLocalStorageDemo:

  @JSExportTopLevel("domLocalStorageDemo")
  def domLocalStorageDemo(in: html.Input, box: html.Div) =
    val key = "my-key"
    in.value = dom.window.localStorage.getItem(key)
    in.onkeyup = (e: dom.Event) =>
      dom.window.localStorage.setItem(key, in.value)
      box.textContent = s"Saved! ${in.value}"
