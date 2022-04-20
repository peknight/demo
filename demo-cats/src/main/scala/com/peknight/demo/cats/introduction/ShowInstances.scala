package com.peknight.demo.cats.introduction

import cats.Show
import cats.syntax.show.*

object ShowInstances:
  given Show[Cat] = Show.show[Cat] {
    cat =>
      val name = cat.name.show
      val age = cat.age.show
      val color = cat.color.show
      s"$name is a $age year-old $color cat."
  }
