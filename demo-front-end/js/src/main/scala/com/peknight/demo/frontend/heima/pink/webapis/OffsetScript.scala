package com.peknight.demo.frontend.heima.pink.webapis

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object OffsetScript:

  @JSExportTopLevel("offset")
  def offset(): Unit =
    val father = dom.document.querySelector(".father").asInstanceOf[dom.HTMLElement]
    val son = dom.document.querySelector(".son").asInstanceOf[dom.HTMLElement]
    val w = dom.document.querySelector(".w").asInstanceOf[dom.HTMLElement]
    logOffset(father)
    logOffset(son)
    logOffset(w)

  def logOffset(element: dom.HTMLElement): Unit =
    println(s"${element.className}: offsetTop=${element.offsetTop} offsetLeft=${element.offsetLeft} " +
      s"offsetWidth=${element.offsetWidth} offsetHeight${element.offsetHeight} offsetParent=${element.offsetParent}")
