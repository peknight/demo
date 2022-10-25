package com.peknight.demo.frontend.heima.pink.javascript.jdpwd

import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.{Attr, Length}

object JingdongPasswordStyles extends StyleSheet.Standalone:
  import dsl.*

  ".box" - (
    position.relative,
    width(400.px),
    borderBottom(1.px, solid, c"#ccc"),
    margin(100.px, auto),
  )

  ".box input" - (
    width(370.px),
    height(30.px),
    border.`0`,
    outline.none,
  )

  ".box img" - (
    position.absolute,
    top(2.px),
    right(2.px),
    width(24.px),
  )