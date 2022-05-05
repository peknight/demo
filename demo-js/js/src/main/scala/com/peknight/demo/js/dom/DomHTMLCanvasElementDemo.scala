package com.peknight.demo.js.dom

import org.scalajs.dom
import org.scalajs.dom.html

import scala.scalajs.js.annotation.JSExportTopLevel

object DomHTMLCanvasElementDemo:
  @JSExportTopLevel("domHTMLCanvasElementDemo")
  def domHTMLCanvasElementDemo(c: html.Canvas) =
    type Ctx2D = dom.CanvasRenderingContext2D
    val ctx = c.getContext("2d").asInstanceOf[Ctx2D]
    val w = 300
    c.width = w
    c.height = w
    ctx.strokeStyle = "red"
    ctx.lineWidth = 3
    ctx.beginPath()
    ctx.moveTo(w / 3, 0)
    ctx.lineTo(w / 3, w / 3)
    ctx.moveTo(w * 2 / 3, 0)
    ctx.lineTo(w * 2 / 3, w / 3)
    ctx.moveTo(w, w / 2)
    ctx.arc(w / 2, w / 2, w / 2, 0, 3.14)
    ctx.stroke()
