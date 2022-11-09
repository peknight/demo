package com.peknight.demo.frontend.heima.pink.jquery

import org.querki.jquery.*
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel
import scalatags.JsDom.all.*

object WeiboPublishScript:

  @JSExportTopLevel("weiboPublish")
  def weiboPublish(): Unit = $(() =>
    $(".btn").on("click", () => {
      val liElement = $(li().render)
      liElement.html(s"${$(".txt").value()}<a href='javascript:;'>删除</a>")
      $("ul").prepend(liElement)
      liElement.slideDown()
      $(".txt").value("")
    })
    $("ul").on("click", "a", null, (element: dom.Element) => {
      val target = $(element).parent()
      target.slideUp(500, () => target.remove())
    })

  )
