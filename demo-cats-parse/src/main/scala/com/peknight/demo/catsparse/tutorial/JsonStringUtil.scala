package com.peknight.demo.catsparse.tutorial

import cats.parse.{Numbers, Parser0, Parser as P}

object JsonStringUtil:

  val decodeTable: Map[Char, Char] = Map(
    '\\' -> '\\',
    '\'' -> '\'',
    '\"' -> '\"',
    // backspace
    'b' -> 8.toChar,
    // form-feed
    'f' -> 12.toChar,
    'n' -> '\n',
    'r' -> '\r',
    't' -> '\t'
  )

  val escapedToken: P[Char] =
    def parseIntStr(p: P[Any], base: Int): P[Char] =
      p.string.map(Integer.parseInt(_, base).toChar)

    val escapes = P.charIn(decodeTable.keys.toSeq).map(decodeTable(_))

    val oct = P.charIn('0' to '7')
    val octP = P.char('o') *> parseIntStr(oct ~ oct, 8)

    val hex = P.charIn(('0' to '9') ++ ('a' to 'f') ++ ('A' to 'F'))
    val hex2 = hex ~ hex
    val hexP = P.char('x') *> parseIntStr(hex2, 16)

    val hex4 = hex2 ~ hex2
    val u4 = P.char('u') *> parseIntStr(hex4, 16)
    val hex8 = hex4 ~ hex4
    val u8 = P.char('U') *> parseIntStr(hex8, 16)

    // do the oneOf in a guess order of likelihood
    val after = P.oneOf(escapes :: u4 :: hexP :: u8 :: octP :: Nil)
    P.char('\\') *> after
  end escapedToken

  def undelimitedString1(endP: P[Unit]): P[String] = escapedToken.orElse((!endP).with1 *> P.anyChar).repAs

  def escapedString(q: Char): P[String] =
    val endP: P[Unit] = P.char(q)
    endP *> undelimitedString1(endP).orElse(P.pure("")) <* endP
  end escapedString
