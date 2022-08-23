package com.peknight.demo.scala.marcos.mirror

import com.peknight.demo.scala.tuples.Employee

import scala.compiletime.{constValue, erasedValue}
import scala.deriving.Mirror

/**
 * 参考：https://blog.philipp-martini.de/blog/magic-mirror-scala3/
 */
object PhilippMartiniMirrorApp extends App:

  inline def labelFromMirror[A](using m: Mirror.Of[A]): String = constValue[m.MirroredLabel]

  println(labelFromMirror[Employee])

  inline def getElemLabels[A <: Tuple]: List[String] = inline erasedValue[A] match
    case _: EmptyTuple => Nil // stop condition - the tuple is empty
    case _: (head *: tail) => // yes, in scala 3 we can match on tuples head and tail to deconstruct them step by step
      val headElementLabel = constValue[head].toString // bring the head label to value space
      val tailElementLabels = getElemLabels[tail] // recursive call to get the labels from the tail
      headElementLabel :: tailElementLabels // concat head + tail

  inline def getElemLabelsHelpler[A](using m: Mirror.Of[A]) =
    getElemLabels[m.MirroredElemLabels] // and call getElemLabels with the elemlabels type

  println(getElemLabelsHelpler[Employee])
