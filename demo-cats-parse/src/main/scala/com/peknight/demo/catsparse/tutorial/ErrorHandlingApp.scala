package com.peknight.demo.catsparse.tutorial

import cats.parse.{Parser, Parser0}
import cats.parse.Parser.char as pchar
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

  /* Soft */

  val searchWord: Parser[String] = alpha.rep.string
  val fieldValue: Parser[(String, Unit)] = alpha.rep.string ~ pchar(':')
  val p6: Parser0[(Option[(String, Unit)], (String, Option[Unit]))] = fieldValue.? ~ (searchWord ~ sp.?)
  println(p6.parse("title:The Wind Has Risen"))
  println(p6.parse("The Wind Has Risen"))
  val p7: Parser0[(Option[(String, Unit)], String)] = fieldValue.? ~ (searchWord ~ sp.?).rep.string
  val p8: Parser[String] = (searchWord ~ sp.?).rep.string
  println((p7.backtrack | p8).parse("title:The Wind Has Risen"))
  println((p7.backtrack | p8).parse("The Wind Has Risen"))

  // 当右侧是一个epsilon failure时可以用soft替代backtrack
  val fieldValueSoft = alpha.rep.string.soft ~ pchar(':')
  val p9 = fieldValueSoft.? ~ (searchWord ~ sp.?).rep.string
  println(p9.parse("title:The Wind Has Risen"))
  println(p9.parse("The Wind Has Risen"))
