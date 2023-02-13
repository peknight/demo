package com.peknight.demo.scalatest.assertion

import org.scalactic.Tolerance.*
import org.scalatest.funsuite.AnyFunSuite

/**
 * 测试错误提示，本测试类中很多测试是不正确的
 */
class AssertionFunSuite extends AnyFunSuite:

  val left = 2
  // val right = 1
  val right = 2
  val a = 1
  // val b = 2
  val b = 1
  // val c = 3
  val c = 4
  val d = 4
  // val xs = List(a, b, c)
  val xs = List(a, b, c, 11)
  // val num = 1.0
  val num = 1
  // val attempted = 2
  val attempted = 1
  val s = "hi"

  inline val codeSnippet1 = "val a: String = 1"
  inline val codeSnippet2 = "val a: Int = 1"

  test("left and right should be equal") {
    assert(left == right)
  }
  test("a should be equal to b OR c should be greater than or equal to d") {
    assert(a == b || c >= d)
  }
  test("xs should contains 4") {
    assert(xs.exists(_ == 4))
  }
  test("\"hello\" should startsWith \"h\" and \"goodbye\" should endsWith \"y\"") {
    // assert("hello".startsWith("h") && "goodbye".endsWith("y"))
    assert("hello".startsWith("h") && "goodbye".endsWith("e"))
  }
  test("num should be instance of Int") {
    assert(num.isInstanceOf[Int])
  }
  test("Some(2) should be empty") {
    // assert(Some(2).isEmpty)
    assert(None.isEmpty)
  }
  test("None should be defined") {
    // assert(None.isDefined)
    assert(Some(2).isDefined)
  }
  test("xs should contains an element greater than 10") {
    assert(xs.exists(i => i > 10))
  }
  test("Execution should be attempted only once") {
    assert(attempted == 1, s"Execution was attempted $left times instead of 1 time")
  }
  test("a minus b should equals -1") {
    // assertResult(-1)(a - b)
    assertResult(0)(a - b)
  }
  test("charAt -1 should throws IndexOutOfBoundsException") {
    assertThrows[IndexOutOfBoundsException](s.charAt(-1))
  }
  test("charAt -1 should throws IndexOutOfBoundsException (intercept version)") {
    val caught = intercept[IndexOutOfBoundsException](s.charAt(-1))
    assert(caught.getMessage.indexOf("-1") != -1)
  }
  test("code snippet #1 should not compile") {
    assertDoesNotCompile(codeSnippet1)
  }
  test("code snippet #1 should not compile because of a type error") {
    assertTypeError(codeSnippet1)
  }
  test("code snippet #2 should compile") {
    assertCompiles(codeSnippet2)
  }
  test("1 + 1 should equals 3") {
    // assert(1 + 1 === 3, "this is a clue")
    assert(1 + 1 === (3 +- 1), "this is a clue")
  }
  test("charAt -1 should throws IndexOutOfBoundsException (withClue version)") {
    withClue("this is a clue") {
      assertThrows[IndexOutOfBoundsException](s.charAt(-1))
    }
  }

end AssertionFunSuite
