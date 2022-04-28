package com.peknight.demo.js.lihaoyi.handson.handson

import cats.effect.{IO, Sync, Temporal}
import cats.syntax.functor.*
import cats.syntax.option.*
import cats.syntax.traverse.*
import com.peknight.demo.js.dom.*
import com.peknight.demo.js.dom.CanvasOps.*
import com.peknight.demo.js.dom.Color.{Blue, Green, Red}
import com.peknight.demo.js.io.IOOps.*
import fs2.{Pure, Stream}
import org.scalajs.dom
import org.scalajs.dom.html
import spire.implicits.*

import scala.concurrent.duration.DurationInt
import scala.scalajs.js.Math.*
import scala.scalajs.js.annotation.JSExportTopLevel

object HandsOnScalaJs:

  type Graph = (Color, Double => Double)

  val graphs: Seq[(Graph, Int)] = Seq[Graph](
    (Red, sin),
    (Green, x => abs(x % 4 - 2) - 1),
    (Blue, x => sin(x/12) * sin(x))
  ).zipWithIndex

  def points(w: Int, h: Int): Stream[Pure, Seq[Point[Double] & Colored]] =
    Stream.unfold(0.0) { s =>
      val x = (s + 1) % w
      // x为零时 返回 点的空列表，此时清屏
      if x == 0 then Some((Seq.empty[Point[Double] & Colored], x))
      else
        // x不为零时，返回要画的点的列表
        val points: Seq[Point[Double] & Colored] =
          for ((color, f), i) <- graphs yield
            val offset = h.toDouble / 3 * (i + 0.5)
            val y = f(x / w * 75) * h / 30
            Point.colored(x, y + offset, color)
        Some((points, x))
      end if
    }

  def drawPoints[F[_]: Sync](points: Seq[Point[Double] & Colored], canvas: html.Canvas): F[Unit] =
    // 点列表为空时清屏
    if points.isEmpty then canvas.clear[F]()
    else
      // 点列表不为空时画点
      val renderer = canvas.context2d
      points.map(renderer.drawSquare(_, 3)).sequence.void
  end drawPoints

  def drawGraphs[F[_]: Sync: Temporal](canvas: html.Canvas): F[Unit] =
    points(canvas.width, canvas.height).evalMap(drawPoints(_, canvas)).metered(20.millis).compile.drain

  @JSExportTopLevel("handsOn")
  def handsOn(canvas: html.Canvas): Unit = drawGraphs[IO](canvas).run()
