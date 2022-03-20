package com.peknight.demo.fpinscala.monoids

/**
 * Word Count
 */
sealed trait WC

object WC {
  case class Stub(chars: String) extends WC
  case class Part(lStub: String, words: Int, rStub: String) extends WC
}
