package com.peknight.demo.js.lihaoyi.handson.canvas

import cats.effect.{Async, Clock, IO, Sync}
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import com.peknight.demo.js.common.dom.CanvasOps.*
import com.peknight.demo.js.common.io.IOOps.*
import com.peknight.demo.js.common.std.Color.{Blue, Green, Red}
import fs2.Stream
import org.scalajs.dom.html

import scala.concurrent.duration.{Duration, DurationInt}
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object CanvasClock:

  def initCanvas[F[_]: Sync](canvas: html.Canvas): F[Unit] =
    val renderer = canvas.context2d
    val gradient = renderer.createLinearGradient(canvas.width / 2 - 100, 0, canvas.width / 2 + 100, 0)
    gradient.addColorStop(0, Red.value)
    gradient.addColorStop(0.5, Green.value)
    gradient.addColorStop(1, Blue.value)
    Sync[F].delay {
      renderer.fillStyle = gradient
      renderer.textAlign = "center"
      renderer.textBaseline = "middle"
      renderer.font = "75px sans-serif"
    }

  def format(time: Duration): String =
    val date = new js.Date(time.toMillis.toDouble)
    f"${date.getHours().toInt}%02d:${date.getMinutes().toInt}%02d:${date.getSeconds().toInt}%02d"

  def show[F[_]: Async](canvas: html.Canvas): F[Unit] =
    for
      _ <- canvas.clear[F]
      realTime <- Clock[F].realTime
      _ <- Sync[F].delay(canvas.context2d.fillText(format(realTime), canvas.width / 2, canvas.height / 2))
    yield ()

  def program[F[_]: Async](canvas: html.Canvas): F[Unit] =
    val renderer = canvas.context2d
    for
      _ <- canvas.resize[F]
      _ <- initCanvas(canvas)
      _ <- Stream.repeatEval(show(canvas)).metered(1.second)
        .compile.drain
    yield ()


  @JSExportTopLevel("canvasClock")
  def canvasClock(canvas: html.Canvas): Unit = program[IO](canvas).run()

