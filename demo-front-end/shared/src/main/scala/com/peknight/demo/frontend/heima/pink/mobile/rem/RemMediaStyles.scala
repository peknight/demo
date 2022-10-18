package com.peknight.demo.frontend.heima.pink.mobile.rem

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

/**
 * 动态设置html标签font-size大小
 * 1. 假设设计稿是750px
 * 2. 假设我们把整个屏幕划分为15等份（划分标准不一可以是20份也可以是10等份）
 * 3. 每一份作为html字体大小，这里就是50px
 * 4. 那么在320px设备的时候，字体大小为 320 / 15 就是21.33px
 * 5. 用我们页面元素的大小除以不同的html字体大小会发现他们比例还是相同的
 * 6. 比如我们以750为标准设计稿
 * 7. 一个100*100像素的页面元素在750屏幕下，就是 100 / 50 转换为rem是 2rem * 2rem 比例是1:1
 * 8. 320屏幕下，html字体大小为21.33，则 2rem = 42.66 但是宽和高的比例还是 1:1
 * 9. 但是已经能实现不同屏幕下 页面元素盒子等比例缩放的效果
 *
 * 元素大小取值方法
 * 1. 最后的公式 页面元素的rem值 = 页面元素值(px) / (屏幕宽度 / 划分的份数)
 * 2. 屏幕宽度 / 划分的份数 就是 html font-size的大小
 * 3. 或者：页面元素的rem值 = 页面元素值(px) / html.font-size字体大小
 */
object RemMediaStyles extends StyleSheet.Inline:
  import dsl.*

  style(
    media.screen.minWidth(320.px)(unsafeRoot("html")(fontSize(21.33.px))),
    media.screen.minWidth(750.px)(unsafeRoot("html")(fontSize(50.px))),
  )
