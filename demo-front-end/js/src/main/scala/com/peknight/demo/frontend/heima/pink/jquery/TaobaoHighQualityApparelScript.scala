package com.peknight.demo.frontend.heima.pink.jquery

import org.querki.jquery.*
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object TaobaoHighQualityApparelScript:

  @JSExportTopLevel("taobaoApparel")
  def taobaoApparel(): Unit = $(() => {
    $("#left li").mouseover((element: dom.Element) => {
      val index = $(element).index()
      dom.console.log(index)
      $("#content div").eq(index).show().siblings().hide()
    })
  })
