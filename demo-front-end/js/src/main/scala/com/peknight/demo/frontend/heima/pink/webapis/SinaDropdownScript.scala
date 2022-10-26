package com.peknight.demo.frontend.heima.pink.webapis

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object SinaDropdownScript:

  @JSExportTopLevel("sinaDropdown")
  def sinaDropdown(): Unit =
    val nav = dom.document.querySelector(".nav").children.foreach { ele =>
      val li = ele.asInstanceOf[dom.HTMLElement]
      li.onmouseover = _ => li.children(1).asInstanceOf[dom.HTMLElement].style.display = "block"
      li.onmouseout = _ => li.children(1).asInstanceOf[dom.HTMLElement].style.display = "none"
    }
