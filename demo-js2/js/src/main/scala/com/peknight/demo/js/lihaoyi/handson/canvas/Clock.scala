package com.peknight.demo.js.lihaoyi.handson.canvas

import com.peknight.demo.js.domain.CanvasSize
import com.peknight.demo.js.util.DomUtil
import org.scalajs.dom
import org.scalajs.dom.html

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object Clock {

  @JSExportTopLevel("canvasClock")
  def canvasClock(canvas: html.Canvas): Unit = {
    val renderer = DomUtil.canvasRenderingContext2D(canvas)
    val CanvasSize(width, height) = DomUtil.canvasSizeByParentElement(canvas)
    canvas.width = width
    canvas.height = height
    val gradient = renderer.createLinearGradient(width / 2 - 100, 0, width / 2 + 100, 0)
    gradient.addColorStop(0, "red")
    gradient.addColorStop(0.5, "green")
    gradient.addColorStop(1, "blue")
    renderer.fillStyle = gradient
    renderer.textAlign = "center"
    renderer.textBaseline = "middle"

    def render() = {
      val date = new js.Date()
      renderer.clearRect(0, 0, width, height)

      renderer.font = "75px sans-serif"
      renderer.fillText(
        Seq(date.getHours(), date.getMinutes(), date.getSeconds()).mkString(":"),
        width / 2,
        height / 2
      )
    }
    dom.window.setInterval(render _, 1000)
  }
}
