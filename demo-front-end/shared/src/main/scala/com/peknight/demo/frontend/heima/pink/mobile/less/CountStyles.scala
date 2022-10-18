package com.peknight.demo.frontend.heima.pink.mobile.less

import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.Macros.Color
import scalacss.internal.{Attr, Length}

object CountStyles extends StyleSheet.Standalone:
  import dsl.*

  val baseFont: Length[Int] = 50.px

  "html" - (
    fontSize(baseFont),
  )

  val border10: Length[Int] = (5 + 5).px

  "div" - (
    width((200 - 50).px),
    height(((200 + 50) * 2).px),
    border(border10, solid, red),
    // scalacss的颜色计算比较麻烦
    backgroundColor(Color(s"#${(0x666 - 0x222).toHexString}")),
  )

  "img" - (
    width(82.rem / baseFont.n),
    height(82.rem / baseFont.n),
  )


