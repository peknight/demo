package com.peknight.demo.frontend.heima.pink.mobile.bilibili

import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.{Attr, Length}

object BilibiliStyles extends StyleSheet.Standalone:
  import dsl.*

  "*" - (
    margin.`0`,
    padding.`0`,
    boxSizing.borderBox,
    Attr.real("-webkit-font-smoothing") := "antialiased",
    Attr.real("-webkit-tap-highlight-color") := rgba(0, 0, 0, 0),
  )

  "body" - (
    fontFamily :=! "Helvetica Neue,Tahoma,Arial,PingFangSC-Regular"
  )

  "a" - (
    color(c"#333"),
    textDecoration := "none",
  )

  "img" - (
    verticalAlign.middle,
    width(100.%%),
    height(100.%%),
  )

  "ul" - (
    listStyle := "none",
  )

  ".ellipsis-2" - (
    overflow.hidden,
    textOverflow := "ellipsis",
    display :=! "-webkit-box",
    Attr.real("-webkit-line-clamp") := "2",
    Attr.real("-webkit-box-orient") := "vertical",
  )


  ".m-navbar" - (
    display.flex,
    width(100.%%),
    height(40.pxToVw),
    justifyContent.spaceBetween,
    alignItems.center,
    padding(`0`, 12.pxToVw, `0`, 18.pxToVw),
    unsafeChild(".logo i")(
      color(c"#fb7299"),
      fontSize(26.pxToVw),
    ),
    unsafeChild(".right")(
      display.flex,
    ),
    unsafeChild(".right a:nth-child(1) i")(
      fontSize(22.pxToVw),
      color(c"#ccc"),
      marginRight(22.pxToVw),
      marginTop(2.pxToVw),
    ),
    unsafeChild(".right a:nth-child(2)")(
      overflow.hidden,
      display.block,
      width(24.pxToVw),
      height(24.pxToVw),
      marginRight(24.pxToVw),
      borderRadius(50.%%),
    ),
    unsafeChild(".right a:nth-child(3)")(
      display.block,
      width(72.pxToVw),
      height(24.pxToVw),
    ),
  )


  extension [A: Numeric] (a: A)
    def pxToVw: Length[Double] = (Numeric[A].toDouble(a) / 3.75).vw
  end extension
