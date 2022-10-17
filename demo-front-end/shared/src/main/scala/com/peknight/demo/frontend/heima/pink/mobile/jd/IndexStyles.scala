package com.peknight.demo.frontend.heima.pink.mobile.jd

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

object IndexStyles extends StyleSheet.Standalone:
  import dsl.*

  "body" - (
    width(100.%%),
    minWidth(320.px),
    maxWidth(640.px),
    margin(`0`, auto),
    fontSize(14.px),
    fontFamily :=! "-apple-system, Helvetica, sans-serif",
    color(c"#666"),
    lineHeight(1.5)
  )
