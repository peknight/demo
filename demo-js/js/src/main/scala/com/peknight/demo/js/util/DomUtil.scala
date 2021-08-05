package com.peknight.demo.js.util

import cats.data.State
import com.peknight.demo.js.domain.CanvasSize
import org.scalajs.dom
import org.scalajs.dom.html

object DomUtil {

  def canvasRenderingContext2D(canvas: html.Canvas): dom.CanvasRenderingContext2D =
    canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

  def canvasSizeByDocumentElement(documentElement: dom.Element): CanvasSize =
    CanvasSize(documentElement.clientWidth, documentElement.clientHeight)

  def canvasSizeByWindow(window: dom.Window): CanvasSize =
    CanvasSize(window.innerWidth.toInt, window.innerHeight.toInt)

  def canvasSizeByParentElement(canvas: html.Canvas): CanvasSize =
    CanvasSize(canvas.parentElement.clientWidth, canvas.parentElement.clientHeight)

  def interval[S, A](window: dom.Window, state: => State[S, A], s: S, timeout: Double): Unit = {
    var current = s
    window.setInterval(() => {
      val next = state.run(current).value._1
      current = next
    }, timeout)
  }
}
