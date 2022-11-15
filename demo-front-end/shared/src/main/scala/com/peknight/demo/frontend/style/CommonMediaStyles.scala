package com.peknight.demo.frontend.style

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

object CommonMediaStyles extends StyleSheet.Inline:

  // 我们此次定义划分的份数为10
  val no: Double = 10

  style(
    // pc端显示最大用750像素的版本，这一行一定要写在最上面
    unsafeRoot("html")(fontSize(50.px)),
  )

  // 设置常见的屏幕尺寸 修改里面的html文字大小
  Seq(320, 360, 375, 384, 400, 414, 424, 480, 540, 720, 750).map(screenSize =>
    style(media.screen.minWidth(screenSize.px)(unsafeRoot("html")(fontSize((screenSize / no).px))))
  )
