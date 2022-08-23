package com.peknight.demo.scala.marcos.inline

object InlineApp extends App:

  inline def logged[T](logger: Logger, x: T): Unit = logger.log(x)

  def notInlineLogged[T](logger: Logger, x: T): Unit = logger.log(x)

  // 内联保留语义，仅有性能差别
  logged(new RefinedLogger, "✔️")
  notInlineLogged(new RefinedLogger, "✔️")

