package com.peknight.demo.js.module.emitting

import scala.collection.mutable
import scala.scalajs.js.annotation.*

object AppB:
  private val x = mutable.Set.empty[String]

  @JSExportTopLevel(name = "start", moduleID = "b")
  def b(): Unit =
    println("hello from b")
    println(x)

  def main(): Unit = x.add("something")
