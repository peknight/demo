package com.peknight.demo.frontend.heima.pink.mobile.ctrip

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

object CtripStyles extends StyleSheet.Standalone:
  import dsl.*

  "body" - (
    maxWidth(540.px),
    minWidth(320.px),
    margin(`0`, auto),
    font := "normal 14px/1.5 Tahoma, \"Lucida Grande\",Verdana,\"Microsoft Yahei\",STXihei,hei",
    color(c"#000"),
    background := "#f2f2f2",
    // 水平滚动条
    overflowX.hidden,
    Attr.real("-webkit-tap-highlight-color") := "transparent",
  )