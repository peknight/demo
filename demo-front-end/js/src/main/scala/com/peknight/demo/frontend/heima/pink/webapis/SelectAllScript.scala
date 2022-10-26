package com.peknight.demo.frontend.heima.pink.webapis

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel

object SelectAllScript:

  @JSExportTopLevel("selectAll")
  def selectAll(): Unit =
    val jCbAll = dom.document.getElementById("j-cb-all").asInstanceOf[dom.HTMLInputElement]
    val jTbs = dom.document.getElementById("j-tb")
      .getElementsByTagName("input").map(_.asInstanceOf[dom.HTMLInputElement])
    jCbAll.onclick = _ => jTbs.foreach(_.checked = jCbAll.checked)
    jTbs.foreach(_.onclick = _ => jCbAll.checked = jTbs.forall(_.checked))
