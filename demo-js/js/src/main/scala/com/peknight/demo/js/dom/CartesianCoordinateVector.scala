package com.peknight.demo.js.dom

import spire.algebra.{EuclideanRing, NRoot}
import spire.syntax.all.*

class CartesianCoordinateVector[U: EuclideanRing: NRoot](val x: U, val y: U) extends Vector[U]:
  def +(v: Vector[U]): Vector[U] = CartesianCoordinateVector(x + v.x, y + v.y)
  def -(v: Vector[U]): Vector[U] = CartesianCoordinateVector(x - v.x, y - v.y)
  def *(u: U): Vector[U] = CartesianCoordinateVector(x * u, y * u)
  def /(u: U): Vector[U] = CartesianCoordinateVector(x equot u, y equot u)
  def length: U = (x * x + y * y).sqrt
