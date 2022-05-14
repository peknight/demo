package com.peknight.demo.js.lihaoyi.upickle.customization

import upickle.default.*

object CustomPicklersApp:

  def customPicklers() =
    println(write(Seq(Wrap(1), Wrap(10), Wrap(100))))
    println(read[Seq[Wrap]]("[1,10,100]"))
    println(write(new CustomThing2(123, "abc")))
    println(read[CustomThing2]("\"123 abc\""))
    println(write(Bar(123, "abc")))
    println(read[Bar]("""["abc",123]"""))

