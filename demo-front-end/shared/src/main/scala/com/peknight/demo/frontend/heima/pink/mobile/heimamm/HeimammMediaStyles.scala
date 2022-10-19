package com.peknight.demo.frontend.heima.pink.mobile.heimamm

import scalacss.ProdDefaults.*

object HeimammMediaStyles extends StyleSheet.Inline:
  import dsl.*

  // 约束当屏幕大于750px时 html字体大小就不变化了
  style(media.screen.minWidth(750.px) (unsafeRoot("html")(fontSize(75.px).important)))
