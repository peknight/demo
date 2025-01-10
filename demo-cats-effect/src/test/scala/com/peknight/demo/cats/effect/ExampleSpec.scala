package com.peknight.demo.cats.effect

import cats.effect.IO
import cats.effect.testing.specs2.CatsEffect
import org.specs2.mutable.Specification

class ExampleSpec extends Specification with CatsEffect:
  "my example" should {
    "make sure IO computes the right result" in {
      IO.pure(1).map(_ + 2).flatMap { result => IO(result mustEqual 3) }
    }
  }
