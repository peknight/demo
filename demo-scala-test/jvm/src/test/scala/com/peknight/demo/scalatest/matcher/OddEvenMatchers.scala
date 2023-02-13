package com.peknight.demo.scalatest.matcher

import org.scalatest.matchers.should.Matchers.not
import org.scalatest.matchers.{BeMatcher, MatchResult}

trait OddEvenMatchers:
  class OddMatcher extends BeMatcher[Int]:
    def apply(left: Int) = MatchResult(
      left % 2 == 1,
      s"$left was even",
      s"$left was odd"
    )
  val odd = new OddMatcher
  val even = not (odd)

object OddEvenMatchers extends OddEvenMatchers
