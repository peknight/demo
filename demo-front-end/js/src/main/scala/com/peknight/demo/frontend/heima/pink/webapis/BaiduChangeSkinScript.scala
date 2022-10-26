package com.peknight.demo.frontend.heima.pink.webapis

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object BaiduChangeSkinScript:

  @JSExportTopLevel("baiduChangeSkin")
  def baiduChangeSkin(): Unit =
    val imgs = dom.document.querySelector(".baidu").querySelectorAll("img")
    imgs.foreach { ele =>
      val img = ele.asInstanceOf[dom.HTMLImageElement]
      img.onclick = _ => dom.document.body.style.backgroundImage = s"url('${img.src}')"
    }
