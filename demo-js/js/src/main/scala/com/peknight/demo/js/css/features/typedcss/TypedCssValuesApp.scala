package com.peknight.demo.js.css.features.typedcss

import scalacss.DevDefaults.*

object TypedCssValuesApp:

  def run =
    println(UntypedDemo.render)
    println("---")
    println(TypedDemo.render)

