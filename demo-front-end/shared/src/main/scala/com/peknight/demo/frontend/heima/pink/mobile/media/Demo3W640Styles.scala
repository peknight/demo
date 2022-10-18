package com.peknight.demo.frontend.heima.pink.mobile.media

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

object Demo3W640Styles extends StyleSheet.Standalone:
  import dsl.*

  "div" - (
    float.left,
    width(50.%%),
    height(100.px),
    &.firstOfType - backgroundColor.pink,
    &.lastOfType - backgroundColor.purple,
  )
