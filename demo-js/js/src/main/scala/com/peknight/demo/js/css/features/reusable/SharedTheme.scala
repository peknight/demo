package com.peknight.demo.js.css.features.reusable

import scalacss.DevDefaults.*

class SharedTheme(using r: StyleSheet.Register) extends StyleSheet.Inline()(r):
  import dsl.*
  val button = style(
    padding(0.5.ex, 2.ex),
    backgroundColor(c"#eee"),
    border(1.px, solid, black)
  )
  val title = style(fontSize(32.px))

