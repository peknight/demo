package com.peknight.demo.frontend.heima.pink.webapis

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object SendMessageScript:

  @JSExportTopLevel("sendMessage")
  def sendMessage(): Unit =
    val btn = dom.document.querySelector("button").asInstanceOf[dom.HTMLButtonElement]
    btn.addEventListener[dom.MouseEvent]("click", _ => {
      btn.disabled = true
      var time = 3
      lazy val timer: Int = dom.window.setInterval(() => {
        if time == 0 then
          dom.window.clearInterval(timer)
          btn.disabled = false
          btn.innerHTML = "发送"
        else
          btn.innerHTML = s"还剩下${time}秒"
          time -= 1
      }, 1000)
      timer
    })
