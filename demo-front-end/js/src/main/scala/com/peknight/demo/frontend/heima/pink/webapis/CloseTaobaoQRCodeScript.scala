package com.peknight.demo.frontend.heima.pink.webapis

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object CloseTaobaoQRCodeScript:

  @JSExportTopLevel("closeTaobaoQRCode")
  def closeTaobaoQRCode(): Unit =
    val btn = dom.document.querySelector(".close-btn").asInstanceOf[dom.HTMLElement]
    val box = dom.document.querySelector(".box").asInstanceOf[dom.HTMLElement]
    btn.onclick = _ => box.style.display = "none"
