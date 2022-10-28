package com.peknight.demo.frontend.heima.pink.webapis

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel
import scala.util.matching.Regex

object PinyougouScript extends App:

  @JSExportTopLevel("pinyougouIndex")
  def pinyougouIndex(): Unit =
    val regex: Regex = ("(?i)(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|" +
      "Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)").r
    if regex.findFirstMatchIn(dom.window.navigator.userAgent).isDefined then dom.window.location.href = "/jingdong"

  @JSExportTopLevel("pinyougouDetail")
  def pinyougouDetail(): Unit =
    ()
