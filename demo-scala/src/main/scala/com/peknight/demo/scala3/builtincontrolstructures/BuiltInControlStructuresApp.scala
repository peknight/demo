package com.peknight.demo.scala3.builtincontrolstructures

import java.io.File
import scala.io.{Source, StdIn}

object BuiltInControlStructuresApp extends App {
  def gcdLoop(x: Long, y: Long): Long =
    var a = x
    var b = y
    while a != 0 do
      val temp = a
      a = b % a
      b = temp
    b

  def doWhileDemo =
    while
      val line = StdIn.readLine()
      println(s"Read: $line")
      line != ""
    do ()

  for i <- 1 to 4 do println(s"Iteration $i")

  for i <- 1 until 4 do println(s"Iteration $i")

  val filesHere = (new File((".")).listFiles())

  def fileLines(file: File) = Source.fromFile(file).getLines().toArray

  def grep(pattern: String) =
    for
      file <- filesHere
      if file.isFile
      if !file.getName.endsWith(".scala")
      line <- fileLines(file)
      trimmed = line.trim
      if trimmed.matches(pattern)
    do println(s"$file: $trimmed")

  grep(".*scala3.*")

  def scalaFiles =
    for
      file <- filesHere
      if file.getName.endsWith(".scala")
    yield file

}
