package com.peknight.demo.frontend.heima.pink.mobile.media

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

object Demo2Styles extends StyleSheet.Standalone:
  import dsl.*

  "*" - (
    margin.`0`,
    padding.`0`,
  )

  ".top" - (
    height(1.rem),
    fontSize(.5.rem),
    backgroundColor.green,
    color.white,
    textAlign.center,
    lineHeight(1.rem),
  )
