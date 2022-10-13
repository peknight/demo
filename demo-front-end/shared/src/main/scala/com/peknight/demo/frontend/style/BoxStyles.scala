package com.peknight.demo.frontend.style

import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*

trait BoxStyles extends StyleSheet.Standalone:
  import dsl.*

  "*" - (
    margin.`0`,
    padding.`0`,
    border(1.px, solid, black),
    color.white,
  )

  ".box" - (
    float.left,
    &.after - style(
      visibility.hidden,
      clear.both,
      display.block,
      content :=! "'.'",
      height.`0`
    )
  )
