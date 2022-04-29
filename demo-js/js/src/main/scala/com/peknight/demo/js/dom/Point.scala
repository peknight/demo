package com.peknight.demo.js.dom

import spire.algebra.{EuclideanRing, NRoot}

trait Point[U] extends Vector[U]:
  def +(v: Vector[U]): Point[U]
  def -(v: Vector[U]): Point[U]
  def *(u: U): Point[U]
  def /(u: U): Point[U]
  def distance(p: Point[U]): U

  override def toString: String = s"Point($x, $y)"

object Point:

  def apply[U: EuclideanRing: NRoot](x: U, y: U): Point[U] = CartesianCoordinatePoint(x, y)
  def unapply[U](p: Point[U]): Option[(U, U)] = Some((p.x, p.y))

  def colored[U: EuclideanRing: NRoot](x: U, y: U, color: Color): Point[U] & Colored = ColoredPoint(x, y, color)

  extension [U: EuclideanRing: NRoot](p: Point[U])
    def colored(color: Color): Point[U] & Colored = ColoredPoint(p.x, p.y, color)
