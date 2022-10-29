package com.peknight.demo.frontend.heima.pink.webapis

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object TaobaoFixedSidebarScript:

  @JSExportTopLevel("taobaoFixedSidebar")
  def taobaoFixedSidebar(): Unit =
    val sliderBar = dom.document.querySelector(".slider-bar").asInstanceOf[dom.HTMLElement]
    val banner = dom.document.querySelector(".banner").asInstanceOf[dom.HTMLElement]
    val main = dom.document.querySelector(".main").asInstanceOf[dom.HTMLElement]
    val goBack = dom.document.querySelector(".go-back").asInstanceOf[dom.HTMLElement]
    val bannerTop = banner.offsetTop
    val sliderBarTop = sliderBar.offsetTop - bannerTop
    val mainTop = main.offsetTop
    dom.document.addEventListener("scroll", _ => {
      if dom.window.pageYOffset >= bannerTop then
        sliderBar.style.position = "fixed"
        sliderBar.style.top = s"${sliderBarTop}px"
      else
        sliderBar.style.position = "absolute"
        sliderBar.style.top = "300px"
      if dom.window.pageYOffset >= mainTop then
        goBack.style.display = "block"
      else
        goBack.style.display = "none"
    })
