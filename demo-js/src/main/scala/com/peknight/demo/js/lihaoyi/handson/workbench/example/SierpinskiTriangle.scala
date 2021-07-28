package com.peknight.demo.js.lihaoyi.handson.workbench.example

import cats.data.State
import cats.syntax.traverse._
import org.scalajs.dom
import org.scalajs.dom.{Element, html}

import scala.scalajs.js.annotation.JSExportTopLevel

object SierpinskiTriangle {

  case class Point(x: Int, y: Int){
    def +(p: Point) = Point(x + p.x, y + p.y)
    def /(d: Int) = Point(x / d, y / d)
  }

  case class Runtime(p: Point, rng: RNG, width: Int, height: Int, count: Int, clear: Boolean)

  // pure

  def nextPoint(threshold: Int): State[Runtime, Point] = State {
    case Runtime(p, rng, width, height, count, clear) => {
      val (index, rng2) = rng.nonNegativeLessThan(3)
      val cornerPoint = index match {
        case 0 => Point(width, height)
        case 1 => Point(0, height)
        case 2 => Point(width / 2, 0)
      }
      val p2 = (p + cornerPoint) / 2
      val count2 = count + 1
      (Runtime(p2, rng2, width, height, count2, clear || count2 % threshold == 0), p2)
    }
  }

  def nextPoints(size: Int, threshold: Int): State[Runtime, List[Point]] = List.fill(size)(nextPoint(threshold)).sequence

  def canvasRenderingContext2D(canvas: html.Canvas): dom.CanvasRenderingContext2D =
    canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

  def screenSize(documentElement: Element): (Int, Int) = (documentElement.clientWidth, documentElement.clientHeight)

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

  def reset(canvas: html.Canvas): State[Runtime, Unit] = State.modify {
    case runtime @ Runtime(p, rng, _, _, count, clear) => {
      if (clear) {
        val (width, height) = screenSize(dom.document.documentElement)
        canvas.width = width
        canvas.height = height
        val ctx = canvasRenderingContext2D(canvas)
        ctx.fillStyle = "gray"
        ctx.fillRect(0, 0, width, height)
        Runtime(p, rng, width, height, count, false)
      } else {
        runtime
      }
    }
  }

  def drawPoint(ctx: dom.CanvasRenderingContext2D, p: Point, width: Int, height: Int): Unit = {
    val (r, g, b) = rgb(p, width, height)
    ctx.fillStyle = s"rgb($g, $r, $b)"
    ctx.fillRect(p.x, p.y, 1, 1)
  }

  def batch(canvas: html.Canvas, batchSize: Int, threshold: Int, interval: Double): State[Runtime, Unit] = for {
    _ <- reset(canvas)
    points <- nextPoints(batchSize, threshold)
    runtime <- State.get[Runtime]
    _ = points.foreach(drawPoint(canvasRenderingContext2D(canvas), _, runtime.width, runtime.height))
    _ = dom.window.setTimeout(() => batch(canvas, batchSize, threshold, interval).run(runtime).value, interval)
  } yield ()

  @JSExportTopLevel("sierpinskiTriangle")
  def sierpinskiTriangle(canvas: html.Canvas): Unit = {
    val (width, height) = screenSize(dom.document.documentElement)
    val runtime = Runtime(Point(0, 0), RNG(0), width, height, 0, true)
    batch(canvas, 10, 10000, 50).run(runtime).value
  }
}
