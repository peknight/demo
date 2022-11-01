package com.peknight.demo.frontend.heima.pink.webapis

import com.peknight.demo.frontend.ftlabs.fastclick.FastClick
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

/**
 * Click延时解决方案 （移动端双击缩放功能 判断双击需要300ms，可在viewport中禁止用户缩放或使用这个tap方法，或使用fastclick插件）
 * 2015年后的移动端浏览器基本没有延时了
 */
object FastClickScript:

  def tap(obj: dom.HTMLElement, callback: () => Unit = () => ()): Unit =
    var move = false
    var startTime = 0L
    obj.addEventListener("touchstart", _ => startTime = System.currentTimeMillis())
    obj.addEventListener("touchmove", _ => move = true)
    obj.addEventListener("touchend", _ => {
      if !move && System.currentTimeMillis() - startTime < 150 then callback()
      move = false
      startTime = 0L
    })

  @JSExportTopLevel("fastClick")
  def fastClick(): Unit =
    dom.document.addEventListener("DOMContentLoaded", _ => FastClick.attach(dom.document.body), false)
    val div = dom.document.querySelector("div")
    div.addEventListener("click", _ => dom.window.alert("11"))
