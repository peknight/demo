package com.peknight.demo.js.css.features.external

import scalacss.DevDefaults.*

object MyStyles extends StyleSheet.Inline:
  import dsl.*
  val button = style(
    addClassNames("btn", "btn-default"), // Bootstrap classes
    textAlign.center // Optional customisation
  )

