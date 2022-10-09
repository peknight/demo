package com.peknight.demo.squants

import squants.space.LengthConversions.*
import squants.space.{Kilometers, Length}
import squants.{DoubleVector, QuantityVector, SVector}

object VectorsApp extends App:
  val vector: QuantityVector[Length] = SVector(Kilometers(1.2), Kilometers(4.3), Kilometers(2.3))
  println(s"vector=$vector")
  // returns the scalar value of the vector
  val magnitude: Length = vector.magnitude
  println(s"magnitude=$magnitude")
  // returns a corresponding vector scaled to 1 of the given unit
  val normalized = vector.normalize(Kilometers)
  println(s"normalized=$normalized")
  val vector2: QuantityVector[Length] = SVector(Kilometers(1.2), Kilometers(5.3), Kilometers(-2.7))
  val vectorSum = vector + vector2
  println(s"vectorSum=$vectorSum")
  val vectorDiff = vector - vector2
  println(s"vectorDiff=$vectorDiff")
  val vectorScaled = vector * 5
  println(s"vectorScaled=$vectorScaled")
  val vectorReduced = vector / 5
  println(s"vectorReduced=$vectorReduced")
  val vectorDouble = vector / 5.meters
  println(s"vectorDouble=$vectorDouble")
  // returns the Dot Product of vector and vectorDouble
  val dotProduct = vector * vectorDouble
  println(s"dotProduct=$dotProduct")
  // currently only supported for 3-dimensional vectors
  val crossProduct = vector crossProduct vectorDouble
  println(s"crossProduct=$crossProduct")

  // a Four-dimensional vector
  val vector3: DoubleVector = SVector(1.2, 4.3, 2.3, 5.4)
