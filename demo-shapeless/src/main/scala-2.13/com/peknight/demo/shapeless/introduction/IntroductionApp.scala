package com.peknight.demo.shapeless.introduction

import shapeless._

object IntroductionApp extends App {
  def employeeCsv(e: Employee): List[String] = List(e.name, e.number.toString, e.manager.toString)
  def iceCreamCsv(c: IceCream): List[String] = List(c.name, c.numCherries.toString, c.inCone.toString)

  // Heterogeneous list (HLists for short)
  val genericEmployee: String :: Int :: Boolean :: HNil = Generic[Employee].to(Employee("Dave", 123, false))
  println(genericEmployee)
  val genericIceCream: String :: Int :: Boolean :: HNil = Generic[IceCream].to(IceCream("Sundae", 1, false))
  println(genericIceCream)

  def genericCsv(gen: String :: Int :: Boolean :: HNil): List[String] =
    List(gen(0), gen(1).toString, gen(2).toString)

  println(genericCsv(genericEmployee))
  println(genericCsv(genericIceCream))
}
