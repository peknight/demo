package com.peknight.demo.frontend.heima.pink.webapis

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object JingdongKeyUpFocusScript:

  @JSExportTopLevel("jdFocus")
  def jingdongKeyUpFocus(): Unit =
    val search = dom.document.querySelector("input").asInstanceOf[dom.HTMLElement]
    dom.document.addEventListener[dom.KeyboardEvent]("keyup", e => if e.keyCode == 83 then search.focus())

