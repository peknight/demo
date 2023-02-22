package com.peknight.demo.scalatest.style

import org.scalatest.funspec.AnyFunSpec

/**
 * The FunSpec style
 *
 * For teams coming from Ruby's RSpec tool, the FunSpec style will feel very familiar; More generally, for any team that
 * prefers BDD, FunSpec's nesting and gentle guide to structuring text (with describe and it) provides an excellent
 * general-purpose choice for writing specification-style tests.
 *
 * To select just the FunSpec style in an sbt build, include this line:
 * libraryDependencies += "org.scalatest" %% "scalatest-funspec" % version % Test
 */
class SetFunSpec extends AnyFunSpec:
  describe("A Set") {
    describe("when empty") {
      it("should have size 0") {
        assert(Set.empty.size == 0)
      }
      it("should produce NoSuchElementException when head is invoked") {
        assertThrows[NoSuchElementException] { Set.empty.head }
      }
    }
  }
