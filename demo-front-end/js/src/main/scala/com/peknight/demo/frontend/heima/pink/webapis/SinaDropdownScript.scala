package com.peknight.demo.frontend.heima.pink.webapis

import org.querki.jquery.*
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object SinaDropdownScript:

  @JSExportTopLevel("sinaDropdown")
  def sinaDropdown(): Unit =
    // 原生js
    // dom.document.querySelector(".nav").children.foreach { ele =>
    //   val li = ele.asInstanceOf[dom.HTMLElement]
    //   li.onmouseover = _ => li.children(1).asInstanceOf[dom.HTMLElement].style.display = "block"
    //   li.onmouseout = _ => li.children(1).asInstanceOf[dom.HTMLElement].style.display = "none"
    // }
    // jQuery
    // $(() => {
    //   // 用slideDown和slideUp虽然有动画效果，但是这玩意好像是在不断调整高度不断触发鼠标离开事件，还是用show和hide吧
    //   $(".nav>li").mouseover((element: dom.Element) => $(element).children("ul").show())
    //   $(".nav>li").mouseout((element: dom.Element) => $(element).children("ul").hide())
    // })
    $(() => {
      $(".nav>li").hover(
        // 如果只写一个函数 那么鼠标经过离开都会触发这个函数
        (element: dom.Element) => $(element).children("ul").show(),
        (element: dom.Element) => $(element).children("ul").hide()
        // stop停止其余排队动画
        // (element: dom.Element) => $(element).children("ul").stop().slideToggle()
      )
    })
