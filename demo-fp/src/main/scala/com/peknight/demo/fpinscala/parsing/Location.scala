package com.peknight.demo.fpinscala.parsing

case class Location(input: String, offset: Int = 0) {
  lazy val line = input.slice(0, offset + 1).count(_ == '\n') + 1
  lazy val col = input.slice(0, offset + 1).lastIndexOf('\n') match {
    case -1 => offset + 1
    case lineStart => offset - lineStart
  }
  def toError(msg: String): ParseError = ParseError(List((this, msg)))

  def advanceBy(n: Int) = copy(offset = offset + n)

  def currentLine: String =
    if (input.length > 1) input.linesIterator.drop(line - 1).next()
    else ""

  def columnCaret = (" " * (col - 1)) + "^"
}
