package com.peknight.demo.shapeless.autoderiving

import com.peknight.demo.shapeless.adtandgeneric.Shape
import com.peknight.demo.shapeless.adtandgeneric.Shape.*
import com.peknight.demo.shapeless.autoderiving.CsvEncoder.{*, given}
import com.peknight.demo.shapeless.derivation.Tree
import com.peknight.demo.shapeless.introduction.{Employee, IceCream}

object AutoDerivingApp extends App:

  val employees: List[Employee] = List(
    Employee("Bill", 1, true),
    Employee("Peter", 2, false),
    Employee("Milton", 3, false)
  )
  println(writeCsv(employees))

  val iceCreams: List[IceCream] = List(
    IceCream("Sundae", 1, false),
    IceCream("Cornetto", 0, true),
    IceCream("Banana Split", 0, false)
  )
  println(writeCsv(iceCreams))

  println(writeCsv(employees zip iceCreams))

  println(summon[CsvEncoder[(String, Int, Boolean)]].encode("abc" *: 123 *: true *: EmptyTuple))

  val shapes: List[Shape] = List(
    Rectangle(3.0, 4.0),
    Circle(1.0)
  )
  println(writeCsv(shapes))

  CsvEncoder[Tree[Int]]
