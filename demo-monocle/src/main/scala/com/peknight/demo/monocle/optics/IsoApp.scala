package com.peknight.demo.monocle.optics

import monocle.Iso

object IsoApp extends App:
  val personToTuple = Iso[Person, (String, Int)](p => (p.name, p.age)){ case (name, age) => Person(name, age) }
  println(personToTuple.get(Person("Zoe", 25)))
  println(personToTuple.reverseGet(("Zoe", 25)))
  println(personToTuple(("Zoe", 25)))

  def listToVector[A] = Iso[List[A], Vector[A]](_.toVector)(_.toList)

  println(listToVector.get(List(1, 2, 3)))

  def vectorToList[A] = listToVector[A].reverse

  println(vectorToList.get(Vector(1, 2, 3)))

  val stringToList = Iso[String, List[Char]](_.toList)(_.mkString(""))

  println(stringToList.modify(_.tail)("Hello"))
