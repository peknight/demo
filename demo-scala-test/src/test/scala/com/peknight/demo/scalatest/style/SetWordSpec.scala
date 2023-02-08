package com.peknight.demo.scalatest.style

import org.scalatest.wordspec.AnyWordSpec

/**
 * The WordSpec style
 *
 * For teams coming from specs or specs2, the WordSpec style will feel familiar, and is often the most natural way to
 * port specsN tests to ScalaTest. AnyWordSpec is very prescriptive in how text must be written, so a good fit for teams
 * who want a high degree of discipline enforced upon their specification text.
 *
 * To select just the WordSpec style in an sbt build, include this line:
 * libraryDependencies += "org.scalatest" %% "scalatest-wordspec" % version % Test
 */
class SetWordSpec extends AnyWordSpec:
  "A Set" when {
    "empty" should {
      "have size 0" in {
        assert(Set.empty.size == 0)
      }
      "produce NoSuchElementException when head is invoked" in {
        assertThrows[NoSuchElementException] { Set.empty.head }
      }
    }
  }
