package com.peknight.demo.js.lihaoyi.upickle.basics

import upickle.default.*

object DefaultsApp:

  def defaults() =
    println(read[FooDefault]("{}"))
    println(read[FooDefault]("""{"i": 123}"""))
    // 如果值与默认值相同，那么upickle会排除它
    println(write(FooDefault(i = 11, s = "lol")))
    println(write(FooDefault(i = 10, s = "lol")))
    println(write(FooDefault()))
