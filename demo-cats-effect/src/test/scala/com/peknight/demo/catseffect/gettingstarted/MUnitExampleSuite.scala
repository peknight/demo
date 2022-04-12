package com.peknight.demo.catseffect.gettingstarted

import cats.effect.IO
import munit.CatsEffectSuite

class MUnitExampleSuite extends CatsEffectSuite {
  test("make sure IO computes the right result") {
    IO.pure(1).map(_ + 2).flatMap { result => IO(assertEquals(result, 3))}
  }
}
