package com.peknight.demo.frontend.heima.pink.webapis

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object JingdongTrackingNumScript:

  @JSExportTopLevel("jdTrackNum")
  def jingdongTrackNum(): Unit =
    val con = dom.document.querySelector(".con").asInstanceOf[dom.HTMLElement]
    val jdInput = dom.document.querySelector(".jd").asInstanceOf[dom.HTMLInputElement]
    jdInput.addEventListener[dom.KeyboardEvent]("keyup", e => {
      if jdInput.value.isEmpty then con.style.display = "none"
      else
        con.style.display = "block"
        con.innerText = jdInput.value
    })
    jdInput.addEventListener("blur", _ => con.style.display = "none")
    jdInput.addEventListener("focus", _ => if jdInput.value.nonEmpty then con.style.display = "block")


