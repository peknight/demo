package com.peknight.demo.cats.introduction

import cats.kernel.Eq
import cats.syntax.eq.*

object EqInstances:

  given catEq: Eq[Cat] = Eq.instance[Cat] { (cat1, cat2) =>
    (cat1.name === cat2.name) &&
      (cat1.age === cat2.age) &&
      (cat1.color == cat2.color)
  }
