package com.peknight.demo.doobie.unittesting


import com.peknight.demo.doobie.unittesting.UnitTestingApp.*
import com.peknight.demo.doobie.xa
import org.scalatest.*

class AnalysisTestScalaCheck extends funsuite.AnyFunSuite with matchers.must.Matchers with doobie.scalatest.IOChecker {

  override val colors = doobie.util.Colors.None

  val transactor = xa

  test("trivial") { check(trivial) }
  test("biggerThan") { check(biggerThan(0)) }
  test("update") { check(update("", "")) }

}
