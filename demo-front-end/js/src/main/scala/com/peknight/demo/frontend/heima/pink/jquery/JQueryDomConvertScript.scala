package com.peknight.demo.frontend.heima.pink.jquery

import org.querki.jquery.*
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object JQueryDomConvertScript:

  @JSExportTopLevel("jQueryDomConvert")
  def jQueryDomConvert(): Unit =
    // 直接获取视频，得到就是jQuery对象
    // $("video")
    val myVideo = dom.document.querySelector("video").asInstanceOf[dom.HTMLVideoElement]
    // jQuery里面没有play这个方法
    // $(myVideo).play()
    // myVideo.play()
    $("video")(0).asInstanceOf[dom.HTMLVideoElement].play()
    $("video").get(0).asInstanceOf[dom.HTMLVideoElement].play()
