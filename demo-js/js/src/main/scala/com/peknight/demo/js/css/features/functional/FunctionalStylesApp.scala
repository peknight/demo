package com.peknight.demo.js.css.features.functional

import scalacss.DevDefaults.*

object FunctionalStylesApp:

  def run =
    println(MyStyles.render)
    println("---")
    println(MyInline.render)
