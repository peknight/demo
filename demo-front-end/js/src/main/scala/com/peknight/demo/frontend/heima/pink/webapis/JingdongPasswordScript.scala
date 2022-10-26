package com.peknight.demo.frontend.heima.pink.webapis

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object JingdongPasswordScript:

  @JSExportTopLevel("jdpwd")
  def jingdongPassword(): Unit =
    // 获取元素
    val eye = dom.document.getElementById("eye").asInstanceOf[dom.HTMLImageElement]
    val pwd = dom.document.getElementById("pwd").asInstanceOf[dom.HTMLInputElement]
    eye.onclick = _ =>
      if pwd.`type` == "password" then
        pwd.`type` = "text"
        eye.src = "/webapis/images/open.png"
      else
        pwd.`type` = "password"
        eye.src = "/webapis/images/close.png"

