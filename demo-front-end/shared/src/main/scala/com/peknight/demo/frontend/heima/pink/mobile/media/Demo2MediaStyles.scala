package com.peknight.demo.frontend.heima.pink.mobile.media

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

object Demo2MediaStyles extends StyleSheet.Inline:
  import dsl.*

  style(
    media.screen.minWidth(320.px) (unsafeRoot("html")(fontSize(50.px))),
    media.screen.minWidth(640.px) (unsafeRoot("html")(fontSize(100.px))),
  )
