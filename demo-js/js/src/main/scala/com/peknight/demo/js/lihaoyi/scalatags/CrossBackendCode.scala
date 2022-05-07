package com.peknight.demo.js.lihaoyi.scalatags

import com.peknight.demo.js.lihaoyi.scalatags.SharedTemplates.{JsTemplates, TextTemplates}
import org.scalajs.dom

object CrossBackendCode:
  val jsVersion: dom.Element = JsTemplates.widget.render
  val txtVersion: String = TextTemplates.widget.render

  def crossBackendCode(): Unit =
    assert(jsVersion.tagName.toLowerCase == "div")
    assert(jsVersion.textContent == "hello")
    assert(txtVersion == "<div>hello</div>")
