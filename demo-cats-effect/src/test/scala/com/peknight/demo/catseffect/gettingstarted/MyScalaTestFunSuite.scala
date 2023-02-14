package com.peknight.demo.catseffect.gettingstarted

import cats.effect.*
import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.flatspec.AsyncFlatSpec

class MyScalaTestFunSuite extends AsyncFlatSpec with AsyncIOSpec with Matchers:
  "My Code" should "works" in {
    IO(1).asserting(_ shouldBe 1)
  }