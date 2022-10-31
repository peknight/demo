package com.peknight.demo.frontend.amfe.flexible

import org.scalajs.dom

import scala.scalajs.js

object Flexible:

  def flexible(window: dom.Window, document: dom.Document): Unit =
    val docEl: dom.HTMLElement = document.documentElement.asInstanceOf[dom.HTMLElement]
    val dpr: Double = Option(window.devicePixelRatio).filter(_ > 0).getOrElse(1)

    // adjust body font size
    def setBodyFontSize(): Unit =
      Option(document.asInstanceOf[dom.HTMLDocument].body)
        .fold(document.addEventListener("DOMContentLoaded", _ => setBodyFontSize()))(
          body => body.style.fontSize = s"${12 * dpr}px"
        )

    setBodyFontSize()

    // set 1rem = viewWidth / 10
    def setRemUnit(): Unit =
      val rem = docEl.clientWidth / 10
      docEl.style.fontSize = s"${rem}px"

    setRemUnit()

    // reset rem unit on page resize
    window.addEventListener("resize", _ => setRemUnit())
    window.addEventListener[dom.PageTransitionEvent]("pageshow", e => if e.persisted then setRemUnit())

    // detect 0.5px supports
    if dpr >= 2 then
      val fakeBody = document.createElement("body")
      val testElement = document.createElement("div").asInstanceOf[dom.HTMLElement]
      testElement.style.border = ".5px solid transparent"
      fakeBody.appendChild(testElement)
      docEl.appendChild(fakeBody)
      if testElement.offsetHeight == 1 then docEl.classList.add("hairlines")
      docEl.removeChild(fakeBody)
