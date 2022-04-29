package com.peknight.demo.js.dom

import spire.algebra.{EuclideanRing, NRoot}

trait Vector[U]:
  def x: U
  def y: U
  def +(v: Vector[U]): Vector[U]
  def -(v: Vector[U]): Vector[U]
  def *(u: U): Vector[U]
  def /(u: U): Vector[U]
  def length: U
  override def toString: String = s"Vector($x, $y)"

object Vector:
  def apply[U: EuclideanRing: NRoot](x: U, y: U): Vector[U] = CartesianCoordinateVector(x, y)
  def unapply[U](v: Vector[U]): Option[(U, U)] = Some((v.x, v.y))
