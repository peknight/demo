package com.peknight.demo.js.css.features.globalregistry

import scalacss.DevDefaults.*

class BoxStyles extends StyleSheet.Inline:
  import dsl.*
  val mainBox = style(
    margin(2.px, 1.ex),
    padding(1.ex),
    border(1.px, solid, black)
  )


