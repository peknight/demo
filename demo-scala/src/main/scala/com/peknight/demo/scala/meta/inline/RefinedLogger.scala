package com.peknight.demo.scala.meta.inline

class RefinedLogger extends Logger:
  override def log(x: Any): Unit = println(s"Any: $x")
  def log(x: String): Unit = println(s"String: $x")

