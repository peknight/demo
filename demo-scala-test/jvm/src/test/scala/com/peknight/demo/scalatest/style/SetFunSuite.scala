package com.peknight.demo.scalatest.style

import org.scalatest.funsuite.AnyFunSuite

/**
 * The FunSuite style
 *
 * For teams coming from xUnit, the FunSuite style feels comfortable and familiar while still giving some of the
 * benefits of BDD: the FunSuite style makes it easy to write descriptive test names, natural to write focused tests,
 * and generates specification-like output that can facilitate communication among stakeholders.
 *
 * To select just the FunSuite style in an sbt build, include this line:
 * libraryDependencies += "org.scalatest" %% "scalatest-funsuite" % version % Test
 */
class SetFunSuite extends AnyFunSuite:
  test("An empty Set should have size 0") {
    assert(Set.empty.size == 0)
  }
  test("Invoking head on an empty Set should produce NoSuchElementException") {
    assertThrows[NoSuchElementException] { Set.empty.head }
  }
