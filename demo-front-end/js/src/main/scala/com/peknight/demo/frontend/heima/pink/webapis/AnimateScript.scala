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
        element.style.left = s"${next(offsetLeft, target)}px"
    }, 15)
    animateTimers.put(element, timer)

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

  private[this] def next(current: Double, target: Double): Int =
    val step = (target - current) / 10
    (current + (if step > 0 then math.ceil(step) else math.floor(step))).toInt
