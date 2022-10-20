package com.peknight.demo.frontend.heima.pink.mobile.responsive

import scalacss.ProdDefaults.*

object ResponsiveMediaStyles extends StyleSheet.Inline:
  import dsl.*

  style(
    // 超小屏幕下 小于768 布局容器的宽度为100%
    media.screen.maxWidth(767.px) (
      unsafeRoot(".container")(width(100.%%)),
      unsafeRoot(".container ul li")(width(33.33.%%))
    ),
    // 小屏幕下 大于等于768 布局容器改为750px
    media.screen.minWidth(768.px) (unsafeRoot(".container")(width(750.px))),
    // 中等屏幕下 992px 布局容器修改为970px
    media.screen.minWidth(992.px) (unsafeRoot(".container")(width(970.px))),
    // 大屏幕下 大于等于1200px 布局容器修改为1170
    media.screen.minWidth(1200.px) (unsafeRoot(".container")(width(1170.px))),
  )
