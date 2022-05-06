package com.peknight.demo.js.common.dom

import cats.effect.Sync
import com.peknight.demo.js.common.std.Color.White
import com.peknight.demo.js.common.std.{Color, Colored, Point}
import org.scalajs.dom
import org.scalajs.dom.html

import scala.scalajs.js

object CanvasOps:

  extension (canvas: html.Canvas)
    def resize[F[_]: Sync]: F[Unit] = Sync[F].delay {
      canvas.width = canvas.getBoundingClientRect().width.toInt
      canvas.height = canvas.getBoundingClientRect().height.toInt
    }

    def context2d: dom.CanvasRenderingContext2D =
      canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

    def clear[F[_] : Sync]: F[Unit] = Sync[F].delay(context2d.clearRect(0, 0, canvas.width, canvas.height))

    def solid[F[_] : Sync](color: Color = White): F[Unit] =
      context2d.withFillStyle[F](color.value)(_.fillRect(0, 0, canvas.width, canvas.height))

  end extension

  extension (renderer: dom.CanvasRenderingContext2D)

    def withFillStyle[F[_] : Sync](fillStyle: js.Any)(f: dom.CanvasRenderingContext2D => Unit): F[Unit] =
      Sync[F].delay {
        val originStyle = renderer.fillStyle
        renderer.fillStyle = fillStyle
        f(renderer)
        renderer.fillStyle = originStyle
      }

    def withColor[F[_] : Sync](color: Color)(f: dom.CanvasRenderingContext2D => Unit): F[Unit] =
      withFillStyle(color.value)(f)

    def drawRect[F[_] : Sync](point: Point[Double] & Colored, width: Double, height: Double): F[Unit] =
      withColor[F](point.color)(_.fillRect(point.x, point.y, width, height))

    def drawSquare[F[_] : Sync](point: Point[Double] & Colored, sideLength: Double): F[Unit] =
      drawRect(point, sideLength, sideLength)
  end extension
