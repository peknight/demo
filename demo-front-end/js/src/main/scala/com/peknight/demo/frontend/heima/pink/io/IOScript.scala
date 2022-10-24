package com.peknight.demo.frontend.heima.pink.io

import org.scalajs.dom
import org.scalajs.dom.document

import scala.scalajs.js.annotation.JSExportTopLevel

object IOScript:

  @JSExportTopLevel("promptDemo")
  def promptDemo(): Unit =
    // 这是一个输入框
    dom.window.prompt("请输入您的年龄")
    // 弹出警示框
    dom.window.alert("计算的结果是：")
    dom.console.log("我是程序员能看到的")
