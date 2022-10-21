package com.peknight.demo.frontend.heima.pink.mobile.bootstrap

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

object BootstrapStyles extends StyleSheet.Standalone:
  import dsl.*

  // 利用我们自己写的样式覆盖原先的样式
  ".login" - (
    width(80.px),
    height(45.px),
    lineHeight(31.px),
  )

  "".attrStartsWith("class", "col") - (
    border(1.px, solid, c"#ccc")
  )

  ".container .row".firstOfType - (
    backgroundColor.pink,
  )
  ".container .row:nth-of-type(n+5)>div" - (
    height(50.px),
    backgroundColor.pink,
    // margin(`0`, 10.px),
  )

  ".container .row:nth-of-type(9)>div" - (
    height(100.px),
    backgroundColor.purple,
    &.nthOfType(3) - (
      backgroundColor.pink,
    ),
    unsafeChild("span")(
      fontSize(50.px),
      color.white,
    )
  )
