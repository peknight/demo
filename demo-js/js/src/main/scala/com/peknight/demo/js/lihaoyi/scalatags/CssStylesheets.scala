package com.peknight.demo.js.lihaoyi.scalatags

import scalatags.JsDom.all.*

/**
 * scalatags的StyleSheet只支持类选择器和伪类选择器，需要层叠的话用CascadingStyleSheet
 */
object CssStylesheets:
  // <div class=" $pkg-Simple-x $pkg-Simple-y"></div>
  val x = div(Simple.x, Simple.y)
