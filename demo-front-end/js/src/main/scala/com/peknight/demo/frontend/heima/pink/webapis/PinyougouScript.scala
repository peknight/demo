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
    dom.window.addEventListener("load", _ => {
      val previewImg = dom.document.querySelector(".preview-img").asInstanceOf[dom.HTMLElement]
      val mask = previewImg.querySelector(".mask").asInstanceOf[dom.HTMLElement]
      val big = previewImg.querySelector(".big").asInstanceOf[dom.HTMLElement]
      previewImg.addEventListener("mouseover", _ => {
        mask.style.display = "block"
        big.style.display = "block"
      })
      previewImg.addEventListener("mouseout", _ => {
        mask.style.display = "none"
        big.style.display = "none"
      })
      previewImg.addEventListener[dom.MouseEvent]("mousemove", e => {
        // 先计算鼠标在盒子内的坐标
        val x = e.pageX - previewImg.offsetLeft
        val y = e.pageY - previewImg.offsetTop
        val maskXMax = previewImg.offsetWidth - mask.offsetWidth
        val maskYMax = previewImg.offsetHeight - mask.offsetHeight
        val maskX = math.min(math.max(x - mask.offsetWidth / 2, 0), maskXMax)
        val maskY = math.min(math.max(y - mask.offsetHeight / 2, 0), maskYMax)
        mask.style.left = s"${maskX}px"
        mask.style.top = s"${maskY}px"
        val bigImg = big.querySelector(".big-img").asInstanceOf[dom.HTMLElement]
        val bigXMax = bigImg.offsetWidth - big.offsetWidth
        val bigYMax = bigImg.offsetHeight - big.offsetHeight
        val bigX = maskX * bigXMax / maskXMax
        val bigY = maskY * bigYMax / maskYMax
        bigImg.style.left = s"${-bigX}px"
        bigImg.style.top = s"${-bigY}px"
      })
    })


