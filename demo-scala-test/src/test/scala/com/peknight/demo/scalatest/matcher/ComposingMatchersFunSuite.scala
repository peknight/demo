package com.peknight.demo.scalatest.matcher

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.{LazyArg, Matcher}
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.matchers.MatcherProducers.*

import java.io.File

class ComposingMatchersFunSuite extends AnyFunSuite:
  def endWithExtension(ext: String) = endWith(ext) compose { (f: File) => f.getPath }

  test("file should endWithExtension txt") {
    new File("output.txt") should endWithExtension("txt")
  }

  val f: Int => Matcher[Int] = be > (_: Int)
  val g: String => Int = (_: String).toInt
  val beAsIntsGreaterThan: String => Matcher[String] = (f compose g) andThen (_ compose g)

  val beAsIntsGreaterThanWithComposeTwice: String => Matcher[String] = f composeTwice g

  val beAsIntsGreaterThanWithErrorMessageModified: String => Matcher[String] = f composeTwice g mapResult { mr =>
    mr.copy(
      failureMessageArgs = mr.failureMessageArgs.map((LazyArg(_) {i => s"""\"${i.toString}\".toInt"""})),
      negatedFailureMessageArgs = mr.negatedFailureMessageArgs.map((LazyArg(_) {i => s"""\"${i.toString}\".toInt"""})),
      midSentenceFailureMessageArgs = mr.midSentenceFailureMessageArgs
        .map((LazyArg(_) {i => s"""\"${i.toString}\".toInt"""})),
      midSentenceNegatedFailureMessageArgs = mr.midSentenceNegatedFailureMessageArgs
        .map((LazyArg(_) {i => s"""\"${i.toString}\".toInt"""}))
    )
  }

  test("modify the type of both sides of a match statement with the same function") {
    "8" should beAsIntsGreaterThan("7")
    "8" should beAsIntsGreaterThanWithComposeTwice("7")
    "8" should beAsIntsGreaterThanWithErrorMessageModified("7")
  }





