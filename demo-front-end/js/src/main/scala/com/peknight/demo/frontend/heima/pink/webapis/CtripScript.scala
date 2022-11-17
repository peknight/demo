package com.peknight.demo.frontend.heima.pink.webapis

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object CtripScript:

  @JSExportTopLevel("ctripFocus")
  def ctripFocus(): Unit = dom.window.addEventListener("load", _ => {
    val focus = dom.document.querySelector(".focus").asInstanceOf[dom.HTMLElement]
    val ul = focus.children.head.asInstanceOf[dom.HTMLElement]
    val ol = focus.children.tail.head.asInstanceOf[dom.HTMLElement]
    val w = focus.offsetWidth
    // 利用定时器自动轮播图图片
    var index = 0
    def translateX(): Unit =
      ul.style.transform = s"translateX(${-index * w}px)"
    def animate(): Unit =
      index += 1
      ul.style.transition = "all .3s"
      translateX()
    var timer = dom.window.setInterval(() => animate(), 2000)
    // 等着我们过渡完成之后，再去判断 监听过渡完成的事件
    ul.addEventListener("transitionend", _ => {
      if index >= 3 then
        index = 0
        ul.style.transition = "none"
        translateX()
      else if index < 0 then
        index = 2
        ul.style.transition = "none"
        translateX()
      ol.querySelector(".current").classList.remove("current")
      ol.children(index).classList.add("current")
    })
    // 手指滑动轮播图
    var startX: Double = 0
    var moveX: Double = 0
    ul.addEventListener[dom.TouchEvent]("touchstart", e => {
      startX = e.targetTouches.head.pageX
      dom.window.clearInterval(timer)
    })
    var flag = false
    ul.addEventListener[dom.TouchEvent]("touchmove", e => {
      moveX = e.targetTouches.head.pageX - startX
      val translateX = -index * w + moveX
      ul.style.transition = "none"
      ul.style.transform = s"translateX(${translateX}px)"
      // 如果用户手指移动过我们再去判断否则不做判断效果
      flag = true
      // 阻止滚动屏幕的行为
      e.preventDefault()
    })
    // 手指离开 可能搞出移动距离去判断是回弹还是播放上一张下一张
    ul.addEventListener[dom.TouchEvent]("touchend", e => {
      if flag then
        // 如果移动距离大于50像素我们就播放上一张或者下一张
        if math.abs(moveX) > 50 then
          if moveX > 0 then index -= 1
          else index += 1
          ul.style.transition = "all .3s"
        else ul.style.transition = "all .1s"
        translateX()
      dom.window.clearInterval(timer)
      timer = dom.window.setInterval(() => animate(), 2000)
    })
    val goBack = dom.document.querySelector(".go-back").asInstanceOf[dom.HTMLElement]
    val nav = dom.document.querySelector("nav").asInstanceOf[dom.HTMLElement]
    dom.window.addEventListener("scroll", _ => {
      if dom.window.pageYOffset >= nav.offsetTop then goBack.style.display = "block"
      else goBack.style.display = "none"
    })
    goBack.addEventListener("click", _ => dom.window.scroll(0, 0))
  })


