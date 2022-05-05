package com.peknight.demo.js.dom

import org.scalajs.dom
import org.scalajs.dom.html

import scala.scalajs.js.annotation.JSExportTopLevel

object NodeOnMouseMoveDemo:

  @JSExportTopLevel("nodeOnMouseMoveDemo")
  def nodeOnMouseMoveDemo(pre: html.Pre) =
    pre.onmousemove = (e: dom.MouseEvent) => pre.textContent =
      s"""
        |e.clientX ${e.clientX}
        |e.clientY ${e.clientY}
        |e.pageX ${e.pageX}
        |e.pageY ${e.pageY}
        |e.screenX ${e.screenX}
        |e.screenY ${e.screenY}
      """.trim.stripMargin
