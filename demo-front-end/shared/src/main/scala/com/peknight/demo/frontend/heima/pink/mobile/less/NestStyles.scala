package com.peknight.demo.frontend.heima.pink.mobile.less

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

object NestStyles extends StyleSheet.Standalone:
  import dsl.*

  ".header" - (
    width(200.px),
    height(200.px),
    backgroundColor.pink,
    // 子元素的样式直接写到父元素里面
    unsafeChild("a")(
      color.red,
      // 如果有伪类、交集选择器、伪元素选择器 我们内层选择器的前面需要加&
      &.hover - color.blue
    ),
  )

  ".nav" - (
    unsafeChild(".logo")(color.green),
    &.before - content.string("")
  )
