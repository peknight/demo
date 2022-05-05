package com.peknight.demo.js.lihaoyi.handson.canvas

import cats.effect.std.Dispatcher
import cats.effect.{Async, IO, Ref, Sync}
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import com.peknight.demo.js.common.dom.CanvasOps.*
import com.peknight.demo.js.common.event.EventListenerOps.*
import com.peknight.demo.js.common.event.EventType.*
import com.peknight.demo.js.common.io.IOOps.*
import com.peknight.demo.js.common.std.Color.{Black, Hex}
import com.peknight.demo.js.common.std.Point
import org.scalajs.dom
import org.scalajs.dom.html
import spire.implicits.*

import scala.concurrent.duration.DurationInt
import scala.scalajs.js.annotation.JSExportTopLevel

object Sketchpad:

  def draw[F[_]: Sync](canvas: html.Canvas, e: dom.MouseEvent, down: Boolean): F[Unit] =
    if down then
      val rect = canvas.getBoundingClientRect()
      canvas.context2d.drawSquare(Point.colored(e.clientX - rect.left, e.clientY - rect.top, Black), 10)
    else Sync[F].unit

  def program[F[_]: Async](canvas: html.Canvas): F[Unit] = Dispatcher[F].use{ dispatcher =>
    given Dispatcher[F] = dispatcher
    for
      _ <- canvas.solid[F](Hex("#f8f8f8"))
      downR <- Ref.of[F, Boolean](false)
      _ <- addEventListener(MouseDown, canvas)(e => downR.update(_ => true))
      _ <- addEventListener(MouseUp, canvas)(e => downR.update(_ => false))
      _ <- addEventListener(MouseMove, canvas) { e =>
        for
          down <- downR.get
          _ <- draw(canvas, e, down)
        yield ()
      }
      _ <- Async[F].never
    yield ()

  }

  @JSExportTopLevel("sketchpad")
  def sketchpad(canvas: html.Canvas) = program[IO](canvas).run()

