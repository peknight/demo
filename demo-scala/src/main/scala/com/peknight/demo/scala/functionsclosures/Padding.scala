package com.peknight.demo.scala.functionsclosures

object Padding:
  def padLines(text: String, minWidth: Int): String =
    def padLine(line: String): String =
      if line.length >= minWidth then line
      else line + " " * (minWidth - line.length)
    val paddedLines =
      for line <- text.linesIterator yield
        padLine(line)
    paddedLines.mkString("\n")

