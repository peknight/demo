package com.peknight.demo.catsparse.tutorial

import cats.parse.Parser
import cats.parse.Rfc5234.{alpha, digit, sp}

object CombiningParsersApp extends App:
  /* Product */

  // the sp parser won't return the whitespace, it just returns Unit if it successful
  val p1: Parser[(Char, Unit)] = alpha ~ sp
  println(p1.parse("t"))
  println(p1.parse("t "))

  /* productL, productR */

  // The type is just Char because we dropping the space
  // to drop the alphabet change the arrow side: alpha *> sp
  val p2: Parser[Char] = alpha <* sp
  println(p2.parse("t"))
  println(p2.parse("t "))

  /* surroundedBy */

  val p3: Parser[Char] = sp *> alpha <* sp
  val p4: Parser[Char] = alpha.surroundedBy(sp)
  println(p3.parse(" a "))
  println(p4.parse(" a "))

  /* between */

  val p5: Parser[Char] = sp *> alpha <* digit
  val p6: Parser[Char] = alpha.between(sp, digit)
  println(p5.parse(" a1"))
  println(p6.parse(" a1"))

  /* OrElse */
  val p7: Parser[AnyVal] = alpha | sp
  println(p7.parse("t"))
  println(p7.parse(" "))
