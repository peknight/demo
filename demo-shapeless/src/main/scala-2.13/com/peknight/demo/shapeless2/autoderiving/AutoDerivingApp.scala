package com.peknight.demo.shapeless2.autoderiving

import com.peknight.demo.shapeless2.adtandgeneric.Shape
import com.peknight.demo.shapeless2.adtandgeneric.Shape.{Circle, Rectangle}
import com.peknight.demo.shapeless2.autoderiving.CsvEncoder._
import com.peknight.demo.shapeless2.introduction.{Employee, IceCream}
import shapeless.HNil

object AutoDerivingApp extends App {
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

  println(reprEncoder.encode("abc" :: 123 :: true :: HNil))

  val shapes: List[Shape] = List(
    Rectangle(3.0, 4.0),
    Circle(1.0)
  )
  println(writeCsv(shapes))

  CsvEncoder[Tree[Int]]
}
