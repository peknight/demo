package com.peknight.demo.js.lihaoyi.upickle.customization

import com.peknight.demo.js.lihaoyi.upickle.basics.Thing

object CustomConfigurationApp:

  def customConfiguration() =
    println(upickle.default.write(Thing(1, "gg")))
    println(upickle.default.read[Thing]("""{"myFieldA":1,"myFieldB":"gg"}"""))
    given SnakePickle.ReadWriter[Thing] = SnakePickle.macroRW
    println(SnakePickle.write(Thing(1, "gg")))
    println(SnakePickle.read[Thing]("""{"my_field_a":1,"my_field_b":"gg"}"""))
    println(upickle.default.write(123: Long))
    println(upickle.default.write(Long.MaxValue))
    println(StringLongs.write(123: Long))
    println(StringLongs.write(Long.MaxValue))
    println(NumLongs.write(123: Long))
    println(NumLongs.write(Long.MaxValue))
