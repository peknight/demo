package com.peknight.demo.js.lihaoyi.handson.webpage

import org.scalajs.dom.html

import scala.scalajs.js.annotation.JSExportTopLevel

object HelloWorld0 {
  @JSExportTopLevel("helloWorld0")
  def helloWorld0(target: html.Div): Unit = {
    val (f, d) = ("fox", "dog")
    target.innerHTML = s"""
    <div>
      <h1>Hello World!</h1>
      <p>
        The quick brown <b>$f</b>
        jumps over the lazy <i>$d</b>
      </p>
    </div>
    """
  }
}
