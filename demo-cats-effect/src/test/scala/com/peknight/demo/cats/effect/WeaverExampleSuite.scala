package com.peknight.demo.cats.effect

import cats.effect.IO
import weaver.SimpleIOSuite

object WeaverExampleSuite extends SimpleIOSuite:
  test("make sure IO computes the right result") {
    IO.pure(1).map(_ + 2).map { result => expect.eql(result, 3) }
  }
