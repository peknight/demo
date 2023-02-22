package com.peknight.demo.scalacheck.effect

import munit.{CatsEffectSuite, ScalaCheckEffectSuite}
import org.scalacheck.effect.PropF
import cats.effect.IO

class ExampleSuite extends CatsEffectSuite with ScalaCheckEffectSuite:
  test("first PropF test") {
    PropF.forAllF { (x: Int) =>
      for
        fiber <- IO(x).start
        outcome <- fiber.join
        _ <- outcome.fold(
          IO(fail("canceled")),
          e => IO(fail("errored", e)),
          fa => fa.map(res => assert(res == x))
        )
      yield ()
    }
  }
