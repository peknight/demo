package com.peknight.demo.frontend.heima.pink.mobile.alibaixiu

import scalacss.ProdDefaults.*

object AlibaixiuMediaStyles extends StyleSheet.Inline:
  import dsl.*

  // 修改container的最大宽度为1280 根据设计稿来走的
  style(
    media.screen.minWidth(1280.px)(unsafeRoot(".container")(width(1280.px))),
    media.screen.maxWidth(767.px)(
      unsafeRoot(".nav ul li")(
        float.left,
        width(20.%%),
      ),
      unsafeRoot(".container .row article")(
        marginTop(10.px),
      ),
    ),
    media.screen.maxWidth(575.px)(
      unsafeRoot(".nav ul a")(
        fontSize(14.px),
        paddingLeft(3.px),
      ),
      unsafeRoot("article .news ul li")(
        width(50.%%),
      ),
      unsafeRoot("article .news ul li:first-of-type")(
        width(100.%%),
      ),
      unsafeRoot(".publish h3")(
        fontSize(14.px),
      )
    ),
  )
