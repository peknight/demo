package com.peknight.demo.frontend.heima.pink.webapis

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object SpriteLoopScript:

  @JSExportTopLevel("spriteLoop")
  def spriteLoop(): Unit =
    val lis = dom.document.querySelectorAll("li")
    lis.zipWithIndex.foreach {
      case (li, index) => li.asInstanceOf[dom.HTMLElement].style.backgroundPosition = s"0 -${index * 44}px"
    }
