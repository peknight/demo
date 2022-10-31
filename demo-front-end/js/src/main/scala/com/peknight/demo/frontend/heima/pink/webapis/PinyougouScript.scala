package com.peknight.demo.frontend.heima.pink.webapis

import com.peknight.demo.frontend.heima.pink.webapis.AnimateScript.animate
import org.scalajs.dom
import scalatags.JsDom.all.*

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

  @JSExportTopLevel("pinyougouFocus")
  def pinyougouFocus(): Unit =
    dom.window.addEventListener("load", _ => {
      val arrowL = dom.document.querySelector(".arrow-l").asInstanceOf[dom.HTMLElement]
      val arrowR = dom.document.querySelector(".arrow-r").asInstanceOf[dom.HTMLElement]
      val focus = dom.document.querySelector(".focus").asInstanceOf[dom.HTMLElement]
      val focusWidth = focus.offsetWidth
      var timerOption: Option[Int] = Some(dom.window.setInterval(() => arrowR.click(), 2000))
      focus.addEventListener("mouseenter", _ => {
        arrowL.style.display = "block"
        arrowR.style.display = "block"
        timerOption.foreach(dom.window.clearInterval)
        timerOption = None
      })
      focus.addEventListener("mouseleave", _ => {
        arrowL.style.display = "none"
        arrowR.style.display = "none"
        timerOption = Some(dom.window.setInterval(() => arrowR.click(), 2000))
      })
      val ulElement = focus.querySelector("ul").asInstanceOf[dom.HTMLElement]
      val olElement = focus.querySelector(".circle").asInstanceOf[dom.HTMLElement]
      olElement.appendChild(frag(List.fill(ulElement.children.length)(li())).render)
      ulElement.appendChild(ulElement.children.head.cloneNode(true))
      val imgCount = ulElement.children.length
      val circles = olElement.children
      circles.head.classList.add("current")
      var imgIndex = 0
      var circleIndex = 0
      circles.zipWithIndex.foreach {
        case (circle, index) => circle.addEventListener("click", _ => {
          circles.foreach(_.classList.remove("current"))
          circle.classList.add("current")
          imgIndex = index
          circleIndex = index
          animate(ulElement, -index * focusWidth)
        })
      }
      var flag = true
      arrowR.addEventListener("click", _ => {
        if flag then
          flag = false
          if imgIndex == imgCount - 1 then
            ulElement.style.left = "0"
            imgIndex = 0
          imgIndex += 1
          animate(ulElement, -imgIndex * focusWidth, () => flag = true)
          circleIndex += 1
          if circleIndex == circles.length then circleIndex = 0
          circleChange(circles, circleIndex)
      })
      arrowL.addEventListener("click", _ => {
        if flag then
          flag = false
          if imgIndex == 0 then
            imgIndex = imgCount - 1
            ulElement.style.left = s"${-imgIndex * focusWidth}px"
          imgIndex -= 1
          animate(ulElement, -imgIndex * focusWidth, () => flag = true)
          circleIndex -= 1
          if circleIndex < 0 then circleIndex = circles.length - 1
          circleChange(circles, circleIndex)
      })
    })

  private[this] def circleChange(circles: dom.HTMLCollection[dom.Element], circleIndex: Int): Unit =
    circles.foreach(_.classList.remove("current"))
    circles(circleIndex).classList.add("current")
