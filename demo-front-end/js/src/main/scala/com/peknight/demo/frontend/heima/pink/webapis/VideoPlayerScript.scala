package com.peknight.demo.frontend.heima.pink.webapis

import com.peknight.demo.frontend.ireaderlab.zymedia.{ZyMedia, ZyMediaOptions}
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object VideoPlayerScript:

  @JSExportTopLevel("videoPlayer")
  def videoPlayer(): Unit =
    ZyMedia.zyMedia("video", new ZyMediaOptions {
      autoplay = false
    })
