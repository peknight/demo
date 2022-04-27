package com.peknight.demo.monocle.prism

import com.peknight.demo.monocle.prism.Json.{JNum, JStr}
import monocle.Prism
import monocle.std.double.doubleToInt

object PrismApp extends App:
  val jStr = Prism[Json, String]{
    case JStr(v) => Some(v)
    case _ => None
  }(JStr.apply)

  val jStr2 = Prism.partial[Json, String]{ case JStr(v) => v }(JStr.apply)

  println(jStr.getOption(JStr("Hello")))
  println(jStr.getOption(JNum(3.2)))

  // 棱镜可以用于模式匹配(实现了unapply方法)
  def isLongString(json: Json): Boolean = json match {
    case jStr(v) => v.length > 100
    case _ => false
  }

  println(jStr.replace("Bar")(JStr("Hello")))
  println(jStr.modify(_.reverse)(JStr("Hello")))

  println(jStr.replace("Bar")(JNum(10)))
  println(jStr.modify(_.reverse)(JNum(10)))

  // Care about the success or failure of the update:
  println(jStr.modifyOption(_.reverse)(JStr("Hello")))
  println(jStr.modifyOption(_.reverse)(JNum(10)))

  val jNum: Prism[Json, Double] = Prism.partial[Json, Double]{ case JNum(v) => v }(JNum.apply)
  val jInt: Prism[Json, Int] = jNum.andThen(doubleToInt)
  println(jInt(5))

  println(jInt.getOption(JNum(5.0)))
  println(jInt.getOption(JNum(5.2)))
  println(jInt.getOption(JStr("Hello")))


