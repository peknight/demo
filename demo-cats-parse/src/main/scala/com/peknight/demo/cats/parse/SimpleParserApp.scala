package com.peknight.demo.cats.parse

import cats.parse.Parser

object SimpleParserApp extends App:
  val p: Parser[Char] = Parser.anyChar
  println(p.parse("t"))
  println(p.parse(""))
  println(p.parse("two"))

