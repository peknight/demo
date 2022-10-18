package com.peknight.demo.frontend.heima.pink.mobile.less

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

object LessStyles extends StyleSheet.Standalone:
  import dsl.*

  // 定义一个粉色的变更
  // @color1: pink;
  val color1 = pink

  // 定义了一个字体为14像素的变量
  // @font14: 14px
  val font14 = 14.px

  // background-color: @color;
  "body" - backgroundColor(color1)

  "div" - (
    color(color1),
    // font-size: @font14;
    fontSize(font14),
  )

  "a" - fontSize(font14)
