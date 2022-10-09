package com.peknight.demo.shapeless.introduction

import scala.deriving.Mirror

object IntroductionApp extends App:
  def employeeCsv(e: Employee): List[String] = List(e.name, e.number.toString, e.manager.toString)
  def iceCreamCsv(c: IceCream): List[String] = List(c.name, c.numCherries.toString, c.inCone.toString)

  val genericEmployee: String *: Int *: Boolean *: EmptyTuple = Tuple.fromProductTyped(Employee("Dave", 123, false))
  println(genericEmployee)
  val genericIceCream: String *: Int *: Boolean *: EmptyTuple = Tuple.fromProductTyped(IceCream("Sundae", 1, false))
  println(genericIceCream)

  def genericCsv(gen: String *: Int *: Boolean *: EmptyTuple): List[String] =
    List(gen(0), gen(1).toString, gen(2).toString)

  println(genericCsv(genericEmployee))
  println(genericCsv(genericIceCream))


