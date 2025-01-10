package com.peknight.demo.cats.parse

import cats.parse.Parser
import cats.parse.Rfc5234.digit

object MappingOutputApp extends App:

  val p: Parser[CharWrapper] = Parser.anyChar.map(char => CharWrapper(char))
  println(p.parse("t"))

  /* String */

  val p2: Parser[String] = digit.map((c: Char) => c.toString)
  // 这个.string看起来和小红书里的slice是一个东西
  val p3: Parser[String] = digit.string
  println(p3.parse("1"))

  /* Unit */

  val p4: Parser[Unit] = digit.map(_ => ())
  val p5: Parser[Unit] = digit.void
  println(p5.parse("1"))

