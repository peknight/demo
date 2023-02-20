package com.peknight.demo.cats.laws

import cats.laws.discipline.FunctorTests
import org.scalacheck.Properties

class TreeLawSpecification extends Properties("Tree"):
  include(FunctorTests[Tree].functor[Int, Int, String].all)
