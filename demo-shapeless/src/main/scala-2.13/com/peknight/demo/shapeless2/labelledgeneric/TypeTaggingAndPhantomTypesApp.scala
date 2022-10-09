package com.peknight.demo.shapeless2.labelledgeneric

import shapeless.Witness
import shapeless.labelled.{FieldType, field}
import shapeless.syntax.singleton._

object TypeTaggingAndPhantomTypesApp extends App {
  val number = 42
  val numCherries = number.asInstanceOf[Int with Cherries]
  val someNumber = 123
  val numCherries2 = "numCherries" ->> someNumber
  println(numCherries2)

  field[Cherries](123)

  def getFieldName[K, V](value: FieldType[K, V])(implicit witness: Witness.Aux[K]): K = witness.value

  println(getFieldName(numCherries2))

  def getFieldValue[K, V](value: FieldType[K, V]): V = value

  println(getFieldValue(numCherries2))
}
