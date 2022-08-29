package com.peknight.demo.shapeless.labelledgeneric

import shapeless.syntax.singleton._

object LiteralTypesApp extends App {
  // 2.13.8 定义成var会报错诶 https://github.com/milessabin/shapeless/issues/674
  val x = 42.narrow
  println(x + 1)
  println(1.narrow)
  println(true.narrow)
  println("hello".narrow)

  // narrow只能用在字面量
  // println(math.sqrt(4).narrow)

  val theAnswer: 42 = 42
  println(theAnswer)
}
