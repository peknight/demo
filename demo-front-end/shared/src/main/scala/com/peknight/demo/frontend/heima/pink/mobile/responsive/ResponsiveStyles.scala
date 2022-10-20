package com.peknight.demo.frontend.heima.pink.mobile.responsive

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

object ResponsiveStyles extends StyleSheet.Standalone:
  import com.peknight.demo.frontend.style.CommonMediaStyles.no
  import dsl.*

  "*" - (
    margin.`0`,
    padding.`0`,
  )

  "ul" - (
    listStyle := "none",
  )

  ".container" - (
    width(750.px),
    margin(`0`, auto),
    backgroundColor.pink,
  )

  ".container ul li" - (
    float.left,
    width(93.75.px),
    height(30.px),
    backgroundColor.green,
  )
