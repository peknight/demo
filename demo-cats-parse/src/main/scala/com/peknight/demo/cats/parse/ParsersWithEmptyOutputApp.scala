package com.peknight.demo.cats.parse

import cats.parse.Parser
import cats.parse.Rfc5234.{alpha, sp}

object ParsersWithEmptyOutputApp extends App:
  val p1: Parser[String] = (alpha.rep <* sp.?).rep.string
  println(p1.parse("hello world"))
  // sp.?会将类型确定为Parser0，而Parser0是不能重复的，这里alpha.rep确定不会为空时，可以用.with1包装一下
  val p2: Parser[String] = (sp.?.with1 *> alpha.rep <* sp.?).rep.string
  println(p2.parse("hello world"))
  println(p2.parse(" hello world"))
  // 如果有多个Parser0在Parser之前，可以用括号括起：`(sp.? ~ sp.?).with1 *> alpha.rep`

