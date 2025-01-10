package com.peknight.demo.cats.parse

import cats.data.NonEmptyList
import cats.parse.Rfc5234.alpha
import cats.parse.{Parser, Parser0}

object RepeatingParsersApp extends App:
  val p1: Parser[NonEmptyList[Char]] = alpha.rep
  val p2: Parser0[List[Char]] = alpha.rep0
  println(p1.parse(""))
  println(p2.parse(""))
  println(p2.parse("something"))

  // 会产生中间列表
  val p3: Parser[String] = alpha.rep.map((l: NonEmptyList[Char]) => l.toList.mkString)
  // p4 p5不会产生中间列表
  val p4: Parser[String] = alpha.rep.string
  val p5: Parser[String] = alpha.repAs[String]
