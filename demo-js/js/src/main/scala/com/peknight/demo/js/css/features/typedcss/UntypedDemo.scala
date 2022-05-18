package com.peknight.demo.js.css.features.typedcss

import scalacss.DevDefaults.*

object UntypedDemo extends StyleSheet.Inline:

  import dsl.*

  val font1 = style(font := "monochrome")
  val font2 = style(font := "'Times New Roman', Times, serif")
  // The DSL provides ^ as a shortcut for Literal.
  val font3 = style(font := ^.auto)


