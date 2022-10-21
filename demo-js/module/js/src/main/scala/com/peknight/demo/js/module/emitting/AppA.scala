package com.peknight.demo.js.module.emitting

import scala.scalajs.js.annotation.*

object AppA:
  // @JSExportTopLevel(name = "start", moduleID = "a")
  def a(): Unit = println("hello from a")

