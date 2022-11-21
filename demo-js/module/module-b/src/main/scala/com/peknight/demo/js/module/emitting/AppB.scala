package com.peknight.demo.js.module.emitting

import scala.collection.mutable
import scala.scalajs.js
import scala.scalajs.js.annotation.*

object AppB:
  private val x = mutable.Set.empty[String]

  @js.native
  @JSImport("./a.js", "startJS")
  def aa(): Unit = js.native

  @JSExportTopLevel(name = "start", moduleID = "b")
  def b(): Unit =
    println("hello from b")
    println(x)
    AppA.a()
    aa()

  def main(): Unit = x.add("something")
