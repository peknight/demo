package com.peknight.demo.js.dom

import spire.algebra.{EuclideanRing, NRoot}
import spire.syntax.all.*

class CartesianCoordinatePoint[U: EuclideanRing: NRoot](override val x: U, override val y: U)
  extends CartesianCoordinateVector[U](x, y) with Point[U]:

  override def +(v: Vector[U]): Point[U] = CartesianCoordinatePoint(x + v.x, y + v.y)
  override def -(v: Vector[U]): Point[U] = CartesianCoordinatePoint(x - v.x, y - v.y)
  override def *(u: U): Point[U] = CartesianCoordinatePoint(x * u, y * u)
  override def /(u: U): Point[U] = CartesianCoordinatePoint(x equot u, y equot u)
  def distance(p: Point[U]): U = (this - p).length