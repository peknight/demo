package com.peknight.demo.js.lihaoyi.upickle.gettingstarted

import upickle.default.*

object GettingStatedApp:

  def gettingStarted() =
    println(write(1))
    println(write(Seq(1, 2, 3)))
    println(read[Seq[Int]]("[1,2,3]"))
    println(write((1, "omg", true)))
    println(read[(Int, String, Boolean)]("""[1,"omg",true]"""))

    println(writeBinary(1).mkString(" "))
    println(writeBinary(Seq(1, 2, 3)).mkString(" "))
    println(readBinary[Seq[Int]](Array[Byte](0x93.toByte, 1, 2, 3)))

    val serializedTuple = Array[Byte](0x93.toByte, 1, 0xa3.toByte, 111, 109, 103, 0xc3.toByte)
    println(writeBinary((1, "omg", true)).mkString(" "))
    println(readBinary[(Int, String, Boolean)](serializedTuple))


