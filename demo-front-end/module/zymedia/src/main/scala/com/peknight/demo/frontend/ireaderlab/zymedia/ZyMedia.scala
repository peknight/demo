package com.peknight.demo.frontend.ireaderlab.zymedia

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal
import org.scalajs.dom.HTMLElement

object ZyMedia:
  type CSSSelector = String
  @js.native
  @JSGlobal("zymedia")
  def zyMedia(selector: CSSSelector | HTMLElement, options: js.UndefOr[ZyMediaOptions] = js.undefined): Unit = js.native
