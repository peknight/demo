package com.peknight.demo.js.lihaoyi.handson.workbench.example

import cats.data.State
import cats.effect.{IO, Sync, Temporal}
import cats.syntax.functor.*
import cats.syntax.traverse.*
import com.peknight.common.core.std.Random
import com.peknight.demo.js.dom.CanvasOps.*
import com.peknight.demo.js.dom.{Colored, Point}
import com.peknight.demo.js.io.IOOps.*
import com.peknight.demo.js.stream.StreamOps
import fs2.Stream
import org.scalajs.dom
import org.scalajs.dom.{Element, html}

import scala.concurrent.duration.*
import scala.scalajs.js.annotation.JSExportTopLevel

object SierpinskiTriangle:

  case class Runtime(random: Random, point: Point)

  def nextPoint(width: Int, height: Int): State[Runtime, Point & Colored] = State { state =>
    val (random, index) = state.random.nextIntBounded(3)
    val cornerPoint = index match
      case 0 => Point(width, height)
      case 1 => Point(0, height)
      case 2 => Point(width / 2, 0)
    val point = (state.point, cornerPoint) match
      case (Point(px, py), Point(cx, cy)) => colored(Point((px + cx) / 2, (py + cy) / 2), width, height)
    (Runtime(random, point), point)
  }

  def colored(point: Point, width: Int, height: Int): Point & Colored =
    val x = point.x * 255 / width
    val y = point.y * 255 / height
    val colorHeight = 512.0 / (255 + y)
    val r = (x * colorHeight).toInt
    val g = ((255 - x) * colorHeight).toInt
    val b = y.toInt
    point.colored(s"rgb($g, $r, $b)")

  def nextPoints(size: Int, width: Int, height: Int): State[Runtime, Seq[Point & Colored]] =
    Seq.fill(size)(nextPoint(width, height)).sequence

  def clear: State[Runtime, Seq[Point & Colored]] = State((_, Seq()))

  def stateStream[F[_]](runtime: Runtime, size: Int, width: Int, height: Int, repeat: Int)
  : Stream[F, Seq[Point & Colored]] =
    val drawStream = Stream(nextPoints(size, width, height)).repeatN(repeat)
    (Stream(clear) ++ drawStream).repeat.through(StreamOps.state(runtime))

  def drawPoints[F[_]: Sync](points: Seq[Point & Colored], canvas: html.Canvas): F[Unit] =
    if points.isEmpty then canvas.clear[F]("black")
    else
      val renderer = canvas.context2d
      points.map(renderer.drawSquare(_, 1)).sequence.void
  end drawPoints

  def program[F[_]: Sync: Temporal](canvas: html.Canvas, runtime: Runtime, size: Int, repeat: Int, rate: FiniteDuration)
  : F[Unit] =
    stateStream[F](runtime, size, canvas.width, canvas.height, repeat)
      .evalMap(drawPoints(_, canvas))
      .metered(rate)
      .compile.drain

  @JSExportTopLevel("sierpinskiTriangle")
  def sierpinskiTriangle(canvas: html.Canvas): Unit =
    program[IO](canvas, Runtime(Random(0), Point(0, 0)), 10, 500, 20.millis).run()
