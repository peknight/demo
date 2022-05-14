package com.peknight.demo.js.lihaoyi.upickle.customization

import com.peknight.demo.js.lihaoyi.upickle.customization.A.B
import upickle.default.*

object CustomKeysApp:

  def customKeys() =
    println(write(KeyBar(10)))
    println(read[KeyBar]("""{"hehehe":10}"""))

    println(write(B(10)))
    println(read[B]("""{"$type":"Bee","i":10}"""))
