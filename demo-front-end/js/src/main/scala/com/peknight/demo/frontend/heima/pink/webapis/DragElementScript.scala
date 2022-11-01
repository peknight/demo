package com.peknight.demo.frontend.heima.pink.webapis

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object DragElementScript:

  @JSExportTopLevel("dragElement")
  def dragElement(): Unit =
    val div = dom.document.querySelector("div").asInstanceOf[dom.HTMLElement]
    var startX: Double = 0
    var startY: Double = 0
    var x: Double = 0
    var y: Double = 0
    div.addEventListener[dom.TouchEvent]("touchstart", e => {
      val targetTouch = e.targetTouches.head
      startX = targetTouch.pageX
      startY = targetTouch.pageY
      x = div.offsetLeft
      y = div.offsetTop
    })
    div.addEventListener[dom.TouchEvent]("touchmove", e => {
      val targetTouch = e.targetTouches.head
      div.style.left = s"${x + targetTouch.pageX - startX}px"
      div.style.top = s"${y + targetTouch.pageY - startY}px"
      e.preventDefault()
    })
