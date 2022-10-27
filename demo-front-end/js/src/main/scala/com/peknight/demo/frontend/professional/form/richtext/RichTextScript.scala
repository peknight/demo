package com.peknight.demo.frontend.professional.form.richtext

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object RichTextScript:
  @JSExportTopLevel("richEditIframe")
  def richEditIframe(): Unit = dom.window.onload = _ =>
    dom.document.querySelector("iframe[name='richedit']")
      .asInstanceOf[dom.HTMLIFrameElement]
      .contentWindow.document.designMode = "on"

  @JSExportTopLevel("richEditContentEditable")
  def richEditContentEditable(): Unit = dom.window.onload = _ =>
    val editable = dom.document.getElementById("richedit").asInstanceOf[dom.HTMLElement]
    editable.contentEditable = "true"
