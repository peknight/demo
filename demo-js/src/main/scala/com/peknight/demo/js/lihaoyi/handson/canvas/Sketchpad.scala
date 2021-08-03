package com.peknight.demo.js.lihaoyi.handson.canvas

import com.peknight.demo.js.domain.CanvasSize
import com.peknight.demo.js.util.DomUtil
import org.scalajs.dom
import org.scalajs.dom.html

import scala.scalajs.js.annotation.JSExportTopLevel

object Sketchpad {

  @JSExportTopLevel("sketchpad")
  def sketchpad(canvas: html.Canvas): Unit = {

    println(s"width=${canvas.parentElement.clientWidth},height=${canvas.parentElement.clientHeight}")

    val CanvasSize(width, height) = DomUtil.canvasSizeByParentElement(canvas)
    canvas.width = width
    canvas.height = height

    val renderer = DomUtil.canvasRenderingContext2D(canvas)
    renderer.fillStyle = "#f8f8f8"
    renderer.fillRect(0, 0, width, height)

    var down = false
    canvas.onmousedown = _ => down = true
    canvas.onmouseup = _ => down = false
    canvas.onmousemove = (e: dom.MouseEvent) => {
      val rect = canvas.getBoundingClientRect()
      if (down) {
        renderer.fillStyle = "black"
        renderer.fillRect(e.clientX - rect.left, e.clientY - rect.top, 10, 10)
      }
    }
  }
}
