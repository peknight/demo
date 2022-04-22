package com.peknight.demo.js.lihaoyi.handson.workbench.example

import cats.data.State
import cats.syntax.traverse._
import com.peknight.demo.js.domain.{CanvasSize, Point, RNG}
import com.peknight.demo.js.util.DomUtil
import org.scalajs.dom
import org.scalajs.dom.{Element, html}

import scala.scalajs.js.annotation.JSExportTopLevel

object SierpinskiTriangle {

  case class Runtime(p: Point, rng: RNG, canvasSize: CanvasSize, count: Int, clear: Boolean)

  // pure

  def nextPoint(threshold: Int): State[Runtime, Point] = State { runtime =>
    val (index, rng) = runtime.rng.nonNegativeLessThan(3)
    val CanvasSize(width, height) = runtime.canvasSize
    val cornerPoint = index match {
      case 0 => Point(width, height)
      case 1 => Point(0, height)
      case 2 => Point(width / 2, 0)
    }
    val p = (runtime.p + cornerPoint) / 2
    val count = runtime.count + 1
    val r = runtime.copy(
      p = p,
      rng = rng,
      count = count,
      clear = runtime.clear || count % threshold == 0
    )
    (r, p)
  }

  def nextPoints(size: Int, threshold: Int): State[Runtime, List[Point]] = List.fill(size)(nextPoint(threshold)).sequence

  def canvasSize(documentElement: Element): CanvasSize = {
//    DomUtil.canvasSize(documentElement)
    CanvasSize(255, 255)
  }

  def rgb(p: Point, width: Int, height: Int): (Int, Int, Int) = {
    val x = p.x * 255.0 / width
    val y = p.y * 255.0 / height
    val colorHeight = 512.0 / (255 + y)
    val r = (x * colorHeight).toInt
    val g = ((255 - x) * colorHeight).toInt
    val b = y.toInt
    (r, g, b)
  }

  // side effect

  def reset(canvas: html.Canvas): State[Runtime, Unit] = State.modify { runtime =>
    if (runtime.clear) {
      val size @ CanvasSize(width, height) = canvasSize(dom.document.documentElement)
      canvas.width = width
      canvas.height = height
      val ctx = DomUtil.canvasRenderingContext2D(canvas)
      ctx.fillStyle = "black"
      ctx.fillRect(0, 0, width, height)
      runtime.copy(canvasSize = size, clear = false)
    } else {
      runtime
    }
  }

  def drawPoint(ctx: dom.CanvasRenderingContext2D, p: Point, canvasSize: CanvasSize): Unit = {
    val (r, g, b) = rgb(p, canvasSize.width, canvasSize.height)
    ctx.fillStyle = s"rgb($g, $r, $b)"
    ctx.fillRect(p.x, p.y, 1, 1)
  }

  def batch(canvas: html.Canvas, batchSize: Int, threshold: Int): State[Runtime, Unit] = for {
    _ <- reset(canvas)
    points <- nextPoints(batchSize, threshold)
    runtime <- State.get[Runtime]
    _ = points.foreach(drawPoint(DomUtil.canvasRenderingContext2D(canvas), _, runtime.canvasSize))
  } yield ()

  @JSExportTopLevel("sierpinskiTriangle")
  def sierpinskiTriangle(canvas: html.Canvas): Unit = {
    val size = canvasSize(dom.document.documentElement)
    val runtime = Runtime(Point(0, 0), RNG(0), size, 0, true)
    DomUtil.interval(dom.window, batch(canvas, 10, 10000), runtime, 50)
  }
}
