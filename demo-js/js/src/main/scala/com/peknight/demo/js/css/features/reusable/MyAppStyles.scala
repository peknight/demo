package com.peknight.demo.js.css.features.reusable

import scalacss.DevDefaults.*

object MyAppStyles extends StyleSheet.Inline:
  import dsl.*

  val theme = new SharedTheme()

  val headingTitle = style(
    theme.title,
    color(red)
  )

