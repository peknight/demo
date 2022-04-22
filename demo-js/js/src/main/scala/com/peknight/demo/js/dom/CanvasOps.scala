package com.peknight.demo.js.dom

import cats.effect.Sync
import org.scalajs.dom
import org.scalajs.dom.html

import scala.scalajs.js

object CanvasOps:

  extension (canvas: html.Canvas)
    def context2d: dom.CanvasRenderingContext2D =
      canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

    def clear[F[_]: Sync](fillStyle: js.Any = "#ffffff"): F[Unit] =
      val renderer = context2d
      renderer.withFillStyle[F](fillStyle)(_.fillRect(0, 0, canvas.width, canvas.height))

  end extension

  extension (renderer: dom.CanvasRenderingContext2D)
    def withFillStyle[F[_]: Sync](fillStyle: js.Any)(f: dom.CanvasRenderingContext2D => Unit): F[Unit] =
      Sync[F].delay {
        val originStyle = renderer.fillStyle
        renderer.fillStyle = fillStyle
        f(renderer)
        renderer.fillStyle = originStyle
      }
  end extension


