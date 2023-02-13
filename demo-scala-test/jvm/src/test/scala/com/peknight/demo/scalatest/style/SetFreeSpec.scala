package com.peknight.demo.scalatest.style

import org.scalatest.freespec.AnyFreeSpec

/**
 * The FreeSpec style
 *
 * Because it gives absolute freedom (and no guidance) on how specification text should be written, the FreeSpec style
 * is a good choice for teams experienced with BDD and able to agree on how to structure the specification text.
 *
 * To select just the FreeSpec style in an sbt build, include this line:
 * libraryDependencies += "org.scalatest" %% "scalatest-freespec" % version % Test
 */
class SetFreeSpec extends AnyFreeSpec:
  "A Set" - {
    "when empty" - {
      "should have size 0" in {
        assert(Set.empty.size == 0)
      }
      "should produce NoSuchElementException when head is invoked" in {
        assertThrows[NoSuchElementException] { Set.empty.head }
      }
    }
  }
