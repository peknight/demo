package com.peknight.demo.js.lihaoyi.handson.workbench.example

import cats.data.State
import cats.syntax.traverse.*
import com.peknight.common.core.std.Random
import com.peknight.demo.js.dom.{Colored, Point}
import fs2.{Pure, Stream}
import org.scalajs.dom
import org.scalajs.dom.{Element, html}

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

  def nextPoints(size: Int, width: Int, height: Int): State[Runtime, Seq[Point & Colored]] =
    Seq.fill(size)(nextPoint(width, height)).sequence

  def colored(point: Point, width: Int, height: Int): Point & Colored =
    val x = point.x * 255 / width
    val y = point.y * 255 / height
    val colorHeight = 512.0 / (255 + y)
    val r = (x * colorHeight).toInt
    val g = ((255 - x) * colorHeight).toInt
    val b = y.toInt
    Point.colored(x, y, s"rgb($g, $r, $b)")

  @JSExportTopLevel("sierpinskiTriangle")
  def sierpinskiTriangle(canvas: html.Canvas): Unit = ()
