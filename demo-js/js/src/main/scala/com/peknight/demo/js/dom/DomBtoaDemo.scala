package com.peknight.demo.js.dom

import org.scalajs.dom
import org.scalajs.dom.html

import scala.scalajs.js.annotation.JSExportTopLevel

/**
 * btoa() The btoa() method creates a Base64-encoded ASCII string from a binary string
 */
object DomBtoaDemo:
  @JSExportTopLevel("domBtoaDemo")
  def domBtoaDemo(in: html.Input, out: html.Div) =
    in.onkeyup = (e: dom.Event) => out.textContent = dom.window.btoa(in.value)
