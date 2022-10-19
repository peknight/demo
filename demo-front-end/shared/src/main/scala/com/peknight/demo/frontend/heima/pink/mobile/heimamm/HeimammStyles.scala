package com.peknight.demo.frontend.heima.pink.mobile.heimamm

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

object HeimammStyles extends StyleSheet.Standalone:
  import com.peknight.demo.frontend.style.CommonMediaStyles.no
  import dsl.*

  val baseFont: Double = 75

  "body" - (
    minWidth(320.px),
    maxWidth(750.px),
    margin(`0`, auto),
    height(1200.px),
    fontFamily :=! "Arial,Helvetica",
  )

  ".header" - (
    height((80 / baseFont).rem),
    borderBottom(1.px, solid, c"#EAEAEA"),
    textAlign.center,
    lineHeight((80 / baseFont).rem),
    fontSize((35 / baseFont).rem),
    color(c"#1c1c1c"),
  )

  ".nav" - (
    display.flex,
    flexWrap.wrap,
    unsafeChild(".item")(
      width(33.33.%%),
    ),
  )

