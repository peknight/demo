package com.peknight.demo.js.lihaoyi.handson.async.advanced

import com.peknight.demo.js.common.dom.CanvasOps.*
import org.scalajs.dom
import org.scalajs.dom.html

import scala.concurrent.*
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("AsyncAdvanced")
object Async:
  def init(canvas: html.Canvas) =
    val renderer = canvas.context2d
    canvas.style.backgroundColor = "#f8f8f8"
    canvas.height = canvas.parentElement.clientHeight
    canvas.width = canvas.parentElement.clientWidth
    renderer.lineWidth = 5
    renderer.strokeStyle = "red"
    renderer.fillStyle = "cyan"
    renderer

  @JSExport
  def main(canvas: html.Canvas) =
    val renderer = init(canvas)
    def rect = canvas.getBoundingClientRect()
    val mousemove = new Channel[dom.MouseEvent](canvas.onmousemove = _)
    val mouseup = new Channel[dom.MouseEvent](canvas.onmouseup = _)
    val mousedown = new Channel[dom.MouseEvent](canvas.onmousedown = _)

    def onMouseMoveOrUp(): Future[Option[dom.MouseEvent]] =
      def mouseMoveOrUp: Future[dom.MouseEvent] = mousemove | mouseup
      def go(future: Future[dom.MouseEvent]): Future[Option[dom.MouseEvent]] =
        future.flatMap(res =>
          if res.`type` == "mousemove" then
            renderer.lineTo(
              res.clientX - rect.left,
              res.clientY - rect.top
            )
            renderer.stroke()
            go(mouseMoveOrUp)
          else Future(Option.empty[dom.MouseEvent])
        )
      go(mouseMoveOrUp)

    def run: Future[Unit] =
      for
        start <- mousedown()
        _ = renderer.beginPath()
        _ = renderer.moveTo(
          start.clientX - rect.left,
          start.clientY - rect.top
        )
        _ <- onMouseMoveOrUp()
        _ = renderer.fill()
        _ <- mouseup()
        _ = renderer.clearRect(0, 0, 1000, 1000)
        _ <- run
      yield ()

    run
  end main


  @JSExport
  def main0(canvas: html.Canvas) =
    val renderer = init(canvas)
    // traditional
    def rect = canvas.getBoundingClientRect()

    var dragState = 0

    canvas.onmousemove = (e: dom.MouseEvent) =>
      if dragState == 1 then
        renderer.lineTo(
          e.clientX - rect.left,
          e.clientY - rect.top
        )
        renderer.stroke()

    canvas.onmouseup = (e: dom.MouseEvent) =>
      if dragState == 1 then
        renderer.fill()
        dragState = 2
      else if dragState == 2 then
        renderer.clearRect(0, 0, 1000, 1000)
        dragState = 0

    canvas.onmousedown = (e: dom.MouseEvent) =>
      if dragState == 0 then
        dragState = 1
        renderer.beginPath()
        renderer.moveTo(
          e.clientX - rect.left,
          e.clientY - rect.top
        )
  end main0

  class Channel[T](init: (T => Unit) => Unit):
    init(update)

    private[this] var value: Promise[T] = null

    def apply(): Future[T] =
      value = Promise[T]()
      value.future

    def update(t: T): Unit = if value != null && !value.isCompleted then value.success(t)

    def |(other: Channel[T]): Future[T] =
      val p = Promise[T]()
      for
        f <- Seq(other(), this())
        t <- f
      do p.trySuccess(t)
      p.future

  end Channel

end Async
