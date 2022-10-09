package com.peknight.demo.frontend.heima.pink.pinyougou.style

import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*

object CommonStyles extends StyleSheet.Standalone:
  import dsl.*

  // 版心
  ".w" - (width(1200.px), margin(`0`, auto))

  ".fl" - float.left
  ".fr" - float.right

  ".style_red" - color(c"#c81623")

  // 快捷导航模块
  ".shortcut" - (height(31.px), lineHeight(31.px), backgroundColor(c"#f1f1f1"))

  ".shortcut ul li" - float.left


