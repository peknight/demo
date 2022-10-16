package com.peknight.demo.frontend.heima.pink.mobile.flowlayout

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

object FlowLayoutStyles extends StyleSheet.Standalone:
  import dsl.*

  "*" - (margin.`0`, padding.`0`)

  "section" - (
    width(100.%%),
    maxWidth(980.px),
    minWidth(320.px),
  )

  "section div" - (
    float.left,
    width(50.%%),
    height(400.px),
    &.nthOfType(1) - backgroundColor.pink,
    &.nthOfType(2) - backgroundColor.purple,
  )

