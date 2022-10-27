package com.peknight.demo.frontend.heima.pink.webapis

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object AngelFollowMouseScript:

  @JSExportTopLevel("angelFollowMouse")
  def angelFollowMouse(): Unit =
    val pic = dom.document.querySelector("img").asInstanceOf[dom.HTMLElement]
    dom.document.addEventListener[dom.MouseEvent]("mousemove", e => {
      val x = e.pageX
      val y = e.pageY
      dom.console.log(s"x坐标是$x，y坐标是$y")
      pic.style.left = s"${x - 50}px"
      pic.style.top = s"${y - 40}px"
    })

    dom.document.addEventListener[dom.KeyboardEvent]("keypress", e => {
      e.metaKey
    })
