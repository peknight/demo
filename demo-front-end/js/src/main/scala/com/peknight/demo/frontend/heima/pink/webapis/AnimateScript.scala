package com.peknight.demo.frontend.heima.pink.webapis

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object AnimateScript:

  def animate(element: dom.HTMLElement, target: Double, callback: () => Unit = () => ()): Unit =
    element.clearTimer
    element.setTimer(dom.window.setInterval(() => {
      val offsetLeft = element.offsetLeft
      if offsetLeft == target then
        element.clearTimer
        callback()
      else
        element.style.left = s"${next(offsetLeft, target)}px"
    }, 15))

  def scroll(target: Double, callback: () => Unit = () => ()): Unit =
    val window = dom.window
    lazy val timer: Int = window.setInterval(() => {
      val pageYOffset = window.pageYOffset
      if pageYOffset == target then
        window.clearInterval(timer)
        callback()
      else
        window.scroll(0, next(pageYOffset, target))
    }, 15)
    timer

  private def next(current: Double, target: Double): Int =
    val step = (target - current) / 10
    (current + (if step > 0 then math.ceil(step) else math.floor(step))).toInt


  extension (element: dom.HTMLElement)
    private def setTimer(timer: Int): Unit = element.asInstanceOf[js.Dynamic].timer = timer
    private def clearTimer: Unit =
      Option(element.asInstanceOf[js.Dynamic].timer.asInstanceOf[Int]).foreach(dom.window.clearInterval)
  end extension