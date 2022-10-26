package com.peknight.demo.frontend.heima.pink.webapis

import org.scalajs.dom
import scalatags.JsDom.all.*

import scala.scalajs.js.annotation.JSExportTopLevel

object PostCommentScript:

  @JSExportTopLevel("postComment")
  def postComment(): Unit =
    val btn = dom.document.querySelector("button").asInstanceOf[dom.HTMLElement]
    val text = dom.document.querySelector("textarea").asInstanceOf[dom.HTMLTextAreaElement]
    val ulElement = dom.document.querySelector("ul")
    btn.onclick = _ => if text.value == "" then dom.window.alert("您没有输入内容") else
      val aElement = a(href := "javascript:;")("删除").render
      val liElement = li(text.value).render
      liElement.append(aElement)
      ulElement.prepend(liElement)
      aElement.onclick = _ => ulElement.removeChild(liElement)
