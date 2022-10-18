package com.peknight.demo.frontend.heima.pink.mobile.suning

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

object SuningStyles extends StyleSheet.Standalone:
  import dsl.*

  "body" - (
    width(100.%%),
    minWidth(320.px),
    maxWidth(640.px),
    margin(`0`, auto),
    fontSize(14.px),
    fontFamily :=! "-apple-system, Helvetica, sans-serif",
    color(c"#666"),
    lineHeight(1.5),
  )

  "*" - (
    Attr.real("-webkit-tap-highlight-color") := "transparent"
  )

  "input" - (
    Attr.real("-webkit-appearance") := "none"
  )

  "img, a" - (
    Attr.real("-webkit-touch-callout") := "none"
  )

  "a" - (
    color(c"#666"),
    textDecoration := "none",
  )

  "ul" - (
    margin.`0`,
    padding.`0`,
    listStyle := "none"
  )

  // 去除底下的空白缝隙
  "img" - verticalAlign.middle

  // css3盒子模型
  "div" - boxSizing.borderBox

  ".clearfix".after - (
    content.string(""),
    display.block,
    lineHeight.`0`,
    visibility.hidden,
    height.`0`,
    clear.both
  )
