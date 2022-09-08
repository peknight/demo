package com.peknight.demo.catsparse.tutorial

import cats.parse.Parser
import cats.parse.Rfc5234.{alpha, digit, sp}

object ErrorHandlingApp extends App:
  val p1: Parser[Char] = alpha
  val p2: Parser[Char] = sp *> alpha

  // epsilon failure 错误时消费了0个字符
  println(p1.parse("123"))
  // arresting failure 错误时消费了至少1个字符 有时也叫halting failure
  println(p2.parse(" 1"))

  /* Backtrack */

  val p3: Parser[Char] = sp *> digit <* sp
  println(p3.parse(" 1"))
  val p4: Parser[Char] = sp *> digit
  println(p3.backtrack.orElse(p4).parse(" 1"))
  println((p3.backtrack | p4).parse(" 1"))
  val p5: Parser[Char] = digit
  println((p3 | p4).parse(" 1"))
  println((p3 | p4 | p5).parse("1"))
