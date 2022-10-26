package com.peknight.demo.frontend.heima.pink.webapis

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object SinaRegisterScript:

  @JSExportTopLevel("sinaRegister")
  def sinaRegister(): Unit =
    val ipt = dom.document.querySelector(".ipt").asInstanceOf[dom.HTMLInputElement]
    val message = dom.document.querySelector(".message")
    ipt.onblur = _ =>
      if ipt.value.length < 6 || ipt.value.length > 16 then
        message.classList.remove("right")
        message.classList.add("wrong")
        message.innerHTML = "您输入的位数不对要求6~16位"
      else
        message.classList.remove("wrong")
        message.classList.add("right")
        message.innerHTML = "您输入的正确"



