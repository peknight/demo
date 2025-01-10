package com.peknight.demo.cats.effect.gettingstarted

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

class MyScalaTestFunSuite extends AsyncFlatSpec with AsyncIOSpec with Matchers:
  "My Code" should "works" in {
    IO(1).asserting(_ shouldBe 1)
  }