package com.peknight.demo.js.lihaoyi.handson.workbench.example

import cats.effect.std.Dispatcher
import cats.effect.{Async, Clock, IO}
import cats.syntax.functor.*
import com.peknight.demo.js.io.IOOps.*
import com.peknight.demo.js.stream.EventTopic.*
import org.scalajs.dom.html

import scala.scalajs.js.annotation.JSExportTopLevel

object DodgeTheDots:

  case class Runtime[F[_]]()

  def program[F[_]: Async](canvas: html.Canvas): F[Unit] = Dispatcher[F].use { dispatcher =>
    for
      startTime <- Clock[F].monotonic
    yield ()
  }

  @JSExportTopLevel("dodgeTheDots")
  def dodgeTheDots(canvas: html.Canvas): Unit = program[IO](canvas).run()

