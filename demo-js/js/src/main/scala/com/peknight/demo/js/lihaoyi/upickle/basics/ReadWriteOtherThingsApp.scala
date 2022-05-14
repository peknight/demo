package com.peknight.demo.js.lihaoyi.upickle.basics

import upickle.default.*

import java.nio.file.Files

object ReadWriteOtherThingsApp:

  def readWriteOtherThings() =
    val original = """{"myFieldA":1,"myFieldB":"gg"}"""
    println(read[Thing](original))
    println(read[Thing](original: CharSequence))
    println(read[Thing](original.getBytes))
    val f = Files.createTempFile("", "")
    Files.write(f, original.getBytes)
    println(read[Thing](f))
    println(read[Thing](f.toFile))
