package com.peknight.demo.frontend.heima.pink.webapis

import org.querki.jquery.*
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object SwitchTableScript:

  @JSExportTopLevel("switchTable")
  def switchTable(): Unit =
    // val tabList = dom.document.querySelectorAll(".tab-list li").map(_.asInstanceOf[dom.HTMLElement])
    // val tabContents = dom.document.querySelectorAll(".tab-con div").map(_.asInstanceOf[dom.HTMLElement])
    // tabList.zip(tabContents).foreach {
    //   case (tab, content) => tab.onclick = _ =>
    //     tabList.foreach(_.classList.remove("current"))
    //     tab.classList.add("current")
    //     tabContents.foreach(_.style.display = "")
    //     content.style.display = "block"
    // }
    $(".tab-list li").click { (element: dom.Element) =>
      val target = $(element)
      target.addClass("current").siblings().removeClass("current")
      $(".tab-con .item").eq(target.index()).show().siblings().hide()
    }
