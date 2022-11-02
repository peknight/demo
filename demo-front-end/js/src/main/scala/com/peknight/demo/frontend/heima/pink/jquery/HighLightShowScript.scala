package com.peknight.demo.frontend.heima.pink.jquery

import org.querki.jquery.*
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object HighLightShowScript:

  @JSExportTopLevel("highLightShow")
  def highLightShow(): Unit = $(() => $(".wrap li").hover(
    (e: JQueryEventObject) => $(e.currentTarget).siblings().stop().fadeTo(400, 0.5),
    (e: JQueryEventObject) => $(e.currentTarget).siblings().stop().fadeTo(400, 1)
  ))
