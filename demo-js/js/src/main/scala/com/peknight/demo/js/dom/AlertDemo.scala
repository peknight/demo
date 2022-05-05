package com.peknight.demo.js.dom

import scala.scalajs.js.annotation.JSExportTopLevel
import org.scalajs.dom

object AlertDemo:

  @JSExportTopLevel("alertDemo")
  def alertDemo() = dom.window.alert("Hi from Scala-js-dom: By alertDemo")

