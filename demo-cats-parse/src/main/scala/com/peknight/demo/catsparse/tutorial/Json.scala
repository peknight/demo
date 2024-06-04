package com.peknight.demo.catsparse.tutorial

import cats.parse.{Numbers, Parser0, Parser as P}
import org.typelevel.jawn.ast.*

object Json:
  private val whitespace: P[Unit] = P.charIn(" \t\r\n").void
  private val whitespaces0: Parser0[Unit] = whitespace.rep0.void


  val parser: P[JValue] = P.recursive[JValue] { recurse =>
    val pnull = P.string("null").as(JNull)
    val bool = P.string("true").as(JBool.True).orElse(P.string("false").as(JBool.False))
    val justStr: P[String] = JsonStringUtil.escapedString('"')
    val str = justStr.map(JString(_))
    val num = Numbers.jsonNumber.map(JNum(_))

    val listSep: P[Unit] =
      P.char(',').soft.surroundedBy(whitespaces0).void

    def rep[A](pa: P[A]): Parser0[List[A]] =
      pa.repSep0(listSep).surroundedBy(whitespaces0)

    val list = rep(recurse).with1
      .between(P.char('['), P.char(']'))
      .map { vs => JArray.fromSeq(vs) }

    val kv: P[(String, JValue)] =
      justStr ~ (P.char(':').surroundedBy(whitespaces0) *> recurse)

    val obj = rep(kv).with1
      .between(P.char('{'), P.char('}'))
      .map { vs => JObject.fromSeq(vs) }

    P.oneOf(str :: num :: list :: obj :: bool :: pnull :: Nil)
  }

  // any whitespace followed by json followed by whitespace followed by end
  val parserFile: P[JValue] = whitespaces0.with1 *> parser <* (whitespaces0 ~ P.end)

