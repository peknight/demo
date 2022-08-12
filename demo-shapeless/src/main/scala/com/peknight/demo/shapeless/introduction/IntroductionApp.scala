package com.peknight.demo.shapeless.introduction

object IntroductionApp extends App {
  def employeeCsv(e: Employee): List[String] = List(e.name, e.number.toString, e.manager.toString)
  def iceCreamCsv(c: IceCream): List[String] = List(c.name, c.numCherries.toString, c.inCone.toString)
}
