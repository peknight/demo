package com.peknight.demo.frontend.heima.pink.webapis

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

/**
 * TouchEvent
 * touches - 正在触摸屏幕的所有手指的一个列表
 * targetTouches - 正在触摸当前DOM元素的手指的一个列表
 * changedTouches 手指状态发生了改变的列表 从无到有 从有到无的变化
 */
object TouchEventScript:

  @JSExportTopLevel("touchEvent")
  def touchEvent(): Unit =
    val div = dom.document.querySelector("div")
    div.addEventListener[dom.TouchEvent]("touchstart", e => dom.console.log(e))
    div.addEventListener[dom.TouchEvent]("touchmove", e => dom.console.log(e))
    div.addEventListener[dom.TouchEvent]("touchend", e => dom.console.log(e))
