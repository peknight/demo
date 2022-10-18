package com.peknight.demo.frontend.heima.pink.mobile.rem

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

object RemStyles extends StyleSheet.Standalone:
  import dsl.*

  "div" - (
    width(2.rem),
    height(2.rem),
    backgroundColor.pink,
  )