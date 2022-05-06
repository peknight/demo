package com.peknight.demo.js.lihaoyi.handson.handson

import cats.effect.{Async, IO, Sync}
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.syntax.traverse.*
import com.peknight.demo.js.common.dom.CanvasOps.*
import com.peknight.demo.js.common.io.IOOps.*
import com.peknight.demo.js.common.std.Color.{Blue, Green, Red}
import com.peknight.demo.js.common.std.{Color, Colored, Point}
import fs2.{Pure, Stream}
import org.scalajs.dom
import org.scalajs.dom.html
import spire.implicits.*

import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.scalajs.js.Math.*
import scala.scalajs.js.annotation.JSExportTopLevel

object HandsOnScalaJs:

  type Graph = (Color, Double => Double)

  val graphs: Seq[(Graph, Int)] = Seq[Graph](
    (Red, sin),
    (Green, x => abs(x % 4 - 2) - 1),
    (Blue, x => sin(x / 12) * sin(x))
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

  def draw[F[_]: Sync](canvas: html.Canvas, points: Seq[Point[Double] & Colored]): F[Unit] =
    // 点列表为空时清屏
    if points.isEmpty then canvas.solid[F]()
    else
      // 点列表不为空时画点
      val renderer = canvas.context2d
      points.map(renderer.drawSquare(_, 3)).sequence.void
  end draw

  def program[F[_]: Async](canvas: html.Canvas, rate: FiniteDuration): F[Unit] =
    for
      _ <- canvas.resize[F]
      _ <- points(canvas.width, canvas.height).evalMap(draw(canvas, _)).metered(rate).compile.drain
    yield ()

  @JSExportTopLevel("handsOn")
  def handsOn(canvas: html.Canvas): Unit = program[IO](canvas, 20.millis).run()
