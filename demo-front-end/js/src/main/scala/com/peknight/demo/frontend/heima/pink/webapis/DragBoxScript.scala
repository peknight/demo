package com.peknight.demo.frontend.heima.pink.webapis

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object DragBoxScript:

  @JSExportTopLevel("dragBox")
  def dragBox(): Unit =
    val login = dom.document.querySelector(".login").asInstanceOf[dom.HTMLElement]
    val mask = dom.document.querySelector(".login-bg").asInstanceOf[dom.HTMLElement]
    val link = dom.document.querySelector("#link").asInstanceOf[dom.HTMLElement]
    val closeBtn = dom.document.querySelector("#closeBtn").asInstanceOf[dom.HTMLElement]
    val title = dom.document.querySelector("#title").asInstanceOf[dom.HTMLElement]

    link.addEventListener("click", _ => {
      mask.style.display = "block"
      login.style.display = "block"
    })
    closeBtn.addEventListener("click", _ => {
      mask.style.display = "none"
      login.style.display = "none"
    })
    title.addEventListener[dom.MouseEvent]("mousedown", e => {
      val x = e.pageX - login.offsetLeft
      val y = e.pageY - login.offsetTop
      val move: js.Function1[dom.MouseEvent, Unit] = e =>
        login.style.left = s"${e.pageX - x}px"
        login.style.top = s"${e.pageY - y}px"
      dom.document.addEventListener("mousemove", move)
      dom.document.addEventListener("mouseup", _ => dom.document.removeEventListener("mousemove", move))
    })
