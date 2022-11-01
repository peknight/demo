package com.peknight.demo.frontend.heima.pink.webapis

import com.peknight.demo.frontend.heima.pink.webapis.AnimateScript.animate
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object SomersaultCloudScript:

  @JSExportTopLevel("somersaultCloud")
  def somersaultCloud(): Unit =
    dom.window.addEventListener("load", _ => {
      val cloud = dom.document.querySelector(".cloud").asInstanceOf[dom.HTMLElement]
      val cNav = dom.document.querySelector(".c-nav").asInstanceOf[dom.HTMLElement]
      val lis = cNav.querySelectorAll("li").map(_.asInstanceOf[dom.HTMLElement])
      var current: Double = 0
      lis.foreach { element =>
        element.addEventListener("mouseenter", _ => animate(cloud, element.offsetLeft))
        element.addEventListener("mouseleave", _ => animate(cloud, current))
        element.addEventListener("click", _ => current = element.offsetLeft)
      }
    })

