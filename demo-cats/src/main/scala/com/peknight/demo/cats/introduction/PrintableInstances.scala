package com.peknight.demo.cats.introduction

import com.peknight.demo.cats.functor.Box

object PrintableInstances:
  given Printable[String] with
    def format(value: String): String = value

  given Printable[Int] with
    def format(value: Int): String = value.toString

  given Printable[Boolean] with
    def format(value: Boolean): String = if value then "yes" else "no"

  given Printable[Cat] with
    def format(cat: Cat): String =
      val name = Printable.format(cat.name)
      val age = Printable.format(cat.age)
      val color = Printable.format(cat.color)
      s"$name is a $age year-old $color cat."

  given boxPrintable[A](using p: Printable[A]): Printable[Box[A]] = p.contramap(_.value)
