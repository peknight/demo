package com.peknight.demo.frontend.heima.pink.jquery

import org.querki.jquery.*
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object HighLightShowScript:

  @JSExportTopLevel("highLightShow")
  def highLightShow(): Unit = $(() => $(".wrap li").hover(
    (element: dom.Element) => $(element).siblings().stop().fadeTo(400, 0.5),
    (element: dom.Element) => $(element).siblings().stop().fadeTo(400, 1)
  ))
