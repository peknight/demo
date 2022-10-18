package com.peknight.demo.frontend.heima.pink.mobile.media

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

object Demo3W320Styles extends StyleSheet.Standalone:
  import dsl.*

  "div" - (
    width(100.%%),
    height(100.px),
    &.firstOfType - backgroundColor.pink,
    &.lastOfType - backgroundColor.purple,
  )
