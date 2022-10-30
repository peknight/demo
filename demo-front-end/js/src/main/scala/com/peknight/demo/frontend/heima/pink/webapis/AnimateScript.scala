package com.peknight.demo.frontend.heima.pink.webapis

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object AnimateScript:

  val animateTimers: collection.mutable.Map[dom.HTMLElement, Int] = collection.mutable.Map.empty[dom.HTMLElement, Int]

  def animate(element: dom.HTMLElement, target: Double, callback: () => Unit = () => ()): Unit =
    animateTimers.get(element).foreach(dom.window.clearInterval)
    val timer = dom.window.setInterval(() => {
      val offsetLeft = element.offsetLeft
      if offsetLeft == target then
        animateTimers.get(element).foreach(dom.window.clearInterval)
        callback()
      else
        val step = (target - offsetLeft) / 10
        val next = offsetLeft + (if step > 0 then math.ceil(step) else math.floor(step))
        element.style.left = s"${next}px"
    }, 15)
    animateTimers.put(element, timer)
