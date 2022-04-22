package com.peknight.demo.js.lihaoyi.handson.handson

import cats.effect.{IO, Sync, Temporal}
import cats.syntax.functor.*
import cats.syntax.option.*
import cats.syntax.traverse.*
import com.peknight.demo.js.dom.*
import com.peknight.demo.js.dom.CanvasOps.*
import com.peknight.demo.js.io.IOOps.*
import fs2.{Pure, Stream}
import org.scalajs.dom
import org.scalajs.dom.html

import scala.concurrent.duration.DurationInt
import scala.scalajs.js.Math.*
import scala.scalajs.js.annotation.JSExportTopLevel

object HandsOnScalaJs:

  type Graph = (String, Double => Double)

  val graphs: Seq[(Graph, Int)] = Seq[Graph](
    ("red", sin),
    ("green", x => abs(x % 4 - 2) - 1),
    ("blue", x => sin(x/12) * sin(x))
  ).zipWithIndex

  def points(w: Int, h: Int): Stream[Pure, Seq[ColoredPoint]] =
    Stream.unfold(0.0) { s =>
      val x = (s + 1) % w
      // x为零时 返回 点的空列表，此时清屏
      if x == 0 then Some((Seq.empty[ColoredPoint], x))
      else
        // x不为零时，返回要画的点的列表
        val points: Seq[ColoredPoint] =
          for ((color, f), i) <- graphs yield
            val offset = h.toDouble / 3 * (i + 0.5)
            val y = f(x / w * 75) * h / 30
            ColoredPoint(x, y + offset, color)
        Some((points, x))
      end if
    }

  // 画单点
  def drawPoint[F[_]: Sync](point: Point & Colored, renderer: dom.CanvasRenderingContext2D): F[Unit] =
    renderer.withFillStyle(point.color)(_.fillRect(point.x, point.y, 3, 3))

  def drawPoints[F[_]: Sync](points: Seq[Point & Colored], canvas: html.Canvas): F[Unit] =
    // 点列表为空时清屏
    if points.isEmpty then canvas.clear[F]()
    else
      // 点列表不为空时画点
      val renderer = canvas.context2d
      points.map(drawPoint(_, renderer)).sequence.void
  end drawPoints

  def drawGraphs[F[_]: Sync: Temporal](canvas: html.Canvas): F[Unit] =
    points(canvas.width, canvas.height).evalMap(drawPoints(_, canvas)).metered(20.millis).compile.drain

  @JSExportTopLevel("handsOn")
  def handsOn(canvas: html.Canvas): Unit = drawGraphs[IO](canvas).run()
