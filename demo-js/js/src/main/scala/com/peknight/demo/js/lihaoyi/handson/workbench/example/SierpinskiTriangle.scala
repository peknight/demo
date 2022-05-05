package com.peknight.demo.js.lihaoyi.handson.workbench.example

import cats.data.State
import cats.effect.*
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.syntax.traverse.*
import com.peknight.common.core.std.Random
import com.peknight.demo.js.common.dom.CanvasOps.*
import com.peknight.demo.js.common.std.Color.RGB
import com.peknight.demo.js.common.std.{Colored, Point}
import com.peknight.demo.js.common.io.IOOps.*
import com.peknight.demo.js.common.stream.StreamPipe.*
import fs2.Stream
import org.scalajs.dom
import org.scalajs.dom.html
import spire.implicits.*

import scala.concurrent.duration.*
import scala.scalajs.js.annotation.JSExportTopLevel

object SierpinskiTriangle:

  case class Runtime(random: Random, point: Point[Double])

  def nextPoint(width: Int, height: Int): State[Runtime, Point[Double] & Colored] = State { state =>
    val (random, index) = state.random.nextIntBounded(3)
    val cornerPoint: Point[Double] = index match
      case 0 => Point(width, height)
      case 1 => Point(0, height)
      case 2 => Point(width / 2, 0)
    val point = colored((state.point + cornerPoint) / 2, width, height)
    (Runtime(random, point), point)
  }

  def colored(point: Point[Double], width: Int, height: Int): Point[Double] & Colored =
    val x = point.x * 255 / width
    val y = point.y * 255 / height
    val colorHeight = 512.0 / (255 + y)
    val r = ((255 - x) * colorHeight).toInt
    val g = (x * colorHeight).toInt
    val b = y.toInt
    point.colored(RGB(r, g, b))

  def nextPoints(size: Int, width: Int, height: Int): State[Runtime, Seq[Point[Double] & Colored]] =
    Seq.fill(size)(nextPoint(width, height)).sequence

  def clear: State[Runtime, Seq[Point[Double] & Colored]] = State((_, Seq()))

  def points[F[_]](runtime: Runtime, size: Int, width: Int, height: Int, repeat: Int)
  : Stream[F, Seq[Point[Double] & Colored]] =
    val drawStream = Stream(nextPoints(size, width, height)).repeatN(repeat)
    (Stream(clear) ++ drawStream).repeat.through(state(runtime))

  def draw[F[_]: Sync](canvas: html.Canvas, points: Seq[Point[Double] & Colored]): F[Unit] =
    if points.isEmpty then canvas.solid[F]()
    else
      val renderer = canvas.context2d
      points.map(renderer.drawSquare(_, 1)).sequence.void
  end draw

  def program[F[_]: Async](canvas: html.Canvas, size: Int, repeat: Int, rate: FiniteDuration)
  : F[Unit] =
    for
      currentTime <- Clock[F].monotonic
      _ <- points[F](Runtime(Random(currentTime.toNanos), Point(0, 0)), size, canvas.width, canvas.height, repeat)
        .evalMap(draw(canvas, _)).metered(rate).compile.drain
    yield ()

  @JSExportTopLevel("sierpinskiTriangle")
  def sierpinskiTriangle(canvas: html.Canvas): Unit = program[IO](canvas, 10, 500, 20.millis).run()
