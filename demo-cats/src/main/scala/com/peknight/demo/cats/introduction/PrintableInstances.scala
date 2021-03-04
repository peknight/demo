package com.peknight.demo.cats.introduction

import com.peknight.demo.cats.functor.Box

object PrintableInstances {
  implicit val stringPrintable: Printable[String] = (value: String) => value

  implicit val intPrintable: Printable[Int] = (value: Int) => value.toString

  implicit val booleanPrintable: Printable[Boolean] = (value: Boolean) => if (value) "yes" else "no"

  implicit val catPrintable: Printable[Cat] = (cat: Cat) => {
    val name = Printable.format(cat.name)
    val age = Printable.format(cat.age)
    val color = Printable.format(cat.color)
    s"${name} is a ${age} year-old ${color} cat."
  }

  implicit def boxPrintable[A](implicit p: Printable[A]): Printable[Box[A]] = p.contramap(_.value)
}
