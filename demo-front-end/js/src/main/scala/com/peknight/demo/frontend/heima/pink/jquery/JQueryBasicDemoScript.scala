package com.peknight.demo.frontend.heima.pink.jquery

import org.querki.jquery.*
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object JQueryBasicDemoScript:

  @JSExportTopLevel("jQueryBasicDemo")
  def jQueryBasicDemo(): Unit = $(() => $("div").hide())
