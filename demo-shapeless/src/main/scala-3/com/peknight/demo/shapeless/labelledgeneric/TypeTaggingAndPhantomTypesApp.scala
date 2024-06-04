package com.peknight.demo.shapeless.labelledgeneric

object TypeTaggingAndPhantomTypesApp extends App:
  val number = 42
  val numCherries = number.asInstanceOf[Int & Cherries]
  // 暂没找到KeyTag的替代

