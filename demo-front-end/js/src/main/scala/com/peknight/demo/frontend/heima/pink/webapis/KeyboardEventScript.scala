package com.peknight.demo.frontend.heima.pink.webapis

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object KeyboardEventScript:

  @JSExportTopLevel("keyboardEvent")
  def keyboardEvent(): Unit =
    dom.document.addEventListener[dom.KeyboardEvent]("keydown", log(_))
    dom.document.addEventListener[dom.KeyboardEvent]("keypress", log(_))
    dom.document.addEventListener[dom.KeyboardEvent]("keyup", log(_))

  def log(e: dom.KeyboardEvent): Unit =
    dom.console.log(s"${e.`type`}: key=${e.key} keyCode=${e.keyCode} ctrl=${e.ctrlKey} alt=${e.altKey} " +
      s"shift=${e.shiftKey} meta=${e.metaKey}")
