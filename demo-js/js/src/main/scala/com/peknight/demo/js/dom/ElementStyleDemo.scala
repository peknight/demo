package com.peknight.demo.js.dom

import com.peknight.demo.js.common.random.Random
import com.peknight.demo.js.common.std.Color.{Blue, Green, Red}
import org.scalajs.dom.html

import scala.scalajs.js.annotation.JSExportTopLevel

object ElementStyleDemo:
  @JSExportTopLevel("elementStyleDemo")
  def elementStyleDemo(div: html.Div) =
    val colors = Seq(Red, Green, Blue)
    val index = Random(System.nanoTime()).nextIntBounded(colors.length)._2
    div.style.color = colors(index).value
