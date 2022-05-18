package com.peknight.demo.js.css.quickstart.inline

import scalacss.DevDefaults.*

object InlineApp:

  def run =
    println(MyStyles.render)
    println("---")
    println(MyStyles.outer.htmlClass)
    println(MyStyles.indent(1).htmlClass)
    println(MyStyles.indent(2).htmlClass)
    println(MyStyles.button.htmlClass)