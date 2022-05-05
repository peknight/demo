package com.peknight.demo.js.lihaoyi.handson.canvas

import cats.effect.{Async, Clock, IO, Sync}
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import com.peknight.demo.js.common.dom.CanvasOps.*
import com.peknight.demo.js.common.io.IOOps.*
import com.peknight.demo.js.common.std.Color.{Blue, Green, Red}
import fs2.Stream
import org.scalajs.dom.html

import scala.concurrent.duration.DurationInt
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object CanvasClock:

  def init[F[_]: Sync](canvas: html.Canvas): F[Unit] =
    val renderer = canvas.context2d
    val gradient = renderer.createLinearGradient(canvas.width / 2 - 100, 0, canvas.width / 2 + 100, 0)
    gradient.addColorStop(0, Red.value)
    gradient.addColorStop(0.5, Green.value)
    gradient.addColorStop(1, Blue.value)
    Sync[F].delay {
      renderer.fillStyle = gradient
      renderer.textAlign = "center"
      renderer.textBaseline = "middle"
    }

  def program[F[_]: Async](canvas: html.Canvas): F[Unit] =
    val renderer = canvas.context2d
    val width = canvas.width
    val height = canvas.height
    for
      _ <- init(canvas)
      _ <- Stream.repeatEval(Clock[F].realTime).evalMap(time => Sync[F].delay {
        val date = new js.Date(time.toMillis.toDouble)
        renderer.clearRect(0, 0, width, height)
        renderer.font = "75px sans-serif"
        renderer.fillText(Seq(date.getHours(), date.getMinutes(), date.getSeconds()).mkString(":"), width / 2, height / 2)
      }).metered(1.second).compile.drain
    yield ()


  @JSExportTopLevel("canvasClock")
  def canvasClock(canvas: html.Canvas): Unit = program[IO](canvas).run()

