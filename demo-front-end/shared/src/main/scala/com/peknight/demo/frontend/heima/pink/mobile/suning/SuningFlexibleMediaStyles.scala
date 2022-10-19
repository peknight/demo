package com.peknight.demo.frontend.heima.pink.mobile.suning

import scalacss.ProdDefaults.*

object SuningFlexibleMediaStyles extends StyleSheet.Inline:
  import dsl.*

  style(media.screen.minWidth(750.px) (unsafeRoot("html")(fontSize(75.px).important)))
