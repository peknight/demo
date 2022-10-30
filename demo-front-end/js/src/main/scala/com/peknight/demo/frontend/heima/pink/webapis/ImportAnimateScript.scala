package com.peknight.demo.frontend.heima.pink.webapis

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object ImportAnimateScript:

  @JSExportTopLevel("importAnimate")
  def importAnimate(): Unit =
    val sliderBar = dom.document.querySelector(".slider-bar").asInstanceOf[dom.HTMLElement]
    val con = dom.document.querySelector(".con").asInstanceOf[dom.HTMLElement]
    sliderBar.addEventListener("mouseenter", _ =>
      AnimateScript.animate(con, -160, () => sliderBar.children.head.innerHTML = "→")
    )
    sliderBar.addEventListener("mouseleave", _ =>
      AnimateScript.animate(con, 0, () => sliderBar.children.head.innerHTML = "←")
    )
