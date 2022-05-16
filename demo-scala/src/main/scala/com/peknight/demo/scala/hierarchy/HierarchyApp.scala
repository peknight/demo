package com.peknight.demo.scala.hierarchy

object HierarchyApp extends App:
  val plumOrApricot: Plum | Apricot = new Plum {}
  val fruit: Fruit = plumOrApricot
  // val doesNotCompile: Plum | Apricot = fruit
  val pluot: Pluot = new Pluot {}
  val plumAndApricot: Plum & Apricot = pluot
  // val doesNotCompile: Pluot = plumAndApricot

  def errorMessage(msg: Int | String): String = msg match
    case n: Int => s"Error number: ${n.abs}"
    case s: String => s"$s!"

  println(errorMessage("Oops"))
  println(errorMessage(-42))
