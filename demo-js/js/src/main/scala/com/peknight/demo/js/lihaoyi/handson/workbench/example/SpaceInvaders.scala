package com.peknight.demo.js.lihaoyi.handson.workbench.example

import cats.effect.std.{Dispatcher, Queue}
import cats.effect.{Async, IO}
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import com.peknight.demo.js.io.IOOps.*
import fs2.Stream
import org.scalajs.dom
import org.scalajs.dom.html

import scala.scalajs.js.annotation.JSExportTopLevel

object SpaceInvaders extends App:

  def onKeyPressStream[F[_]: Async](dispatcher: Dispatcher[F]): F[Stream[F, dom.KeyboardEvent]] =
    for
      queue <- Queue.unbounded[F, dom.KeyboardEvent]
      _ <- Async[F].delay { dom.document.onkeypress = e => dispatcher.unsafeToPromise(queue.offer(e)) }
    yield Stream.fromQueueUnterminated(queue)

  def program[F[_]: Async] = Dispatcher[F].use { dispatcher =>
    for
      stream <- onKeyPressStream(dispatcher)
      _ <- stream.compile.drain
    yield ()
  }

  @JSExportTopLevel("spaceInvaders")
  def spaceInvaders(canvas: html.Canvas): Unit = program[IO].run()



