package com.peknight.demo.scala3.typeclasses

object TypeClassesApp extends App:
  val appleTwo = Apple(2)
  val appleTwoToo = Apple(2)
  println(appleTwo == appleTwoToo)
  val orangeTwo = Orange(2)
  // println(appleTwo == orangeTwo)
  val street = Street("123 Main St")
  import scala.language.implicitConversions
  val streetStr: String = street
  println(streetStr)

