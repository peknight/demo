package com.peknight.demo.frontend.heima.pink.jquery

import org.querki.jquery.*
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object AccordionScript:

  @JSExportTopLevel("accordion")
  def accordion(): Unit = $(() => $(".king li").mouseenter((e: JQueryEventObject) => {
    val target = $(e.currentTarget)
    target.stop().animate(js.Dictionary[js.Any]("width" -> 224), 1000)
      .find(".small").stop().fadeOut()
      .siblings(".big").stop().fadeIn()
    target.siblings("li").stop().animate(js.Dictionary[js.Any]("width" -> 69), 1000)
      .find(".small").stop().fadeIn()
      .siblings(".big").stop().fadeOut()
  }))
