package com.peknight.demo.frontend.heima.pink.mobile.viewportwidth

import scalacss.ProdDefaults.*
import scalacss.internal.{Attr, Length}
import scalacss.internal.Dsl.*

object ViewportWidthStyles extends StyleSheet.Standalone:
  import dsl.*

  "div" - (
    width((50 / 3.75).vw),
    height((50 / 3.75).vw),
    backgroundColor.pink,
    fontSize((16 / 3.75).vw),
  )

