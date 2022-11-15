package com.peknight.demo.frontend.heima.pink.javascript.datavisualization

import scalacss.ProdDefaults.*
import scalacss.internal.Length

object DataVisualizationMediaStyles extends StyleSheet.Inline:
  import com.peknight.demo.frontend.style.CommonMediaStyles.no
  import dsl.*

  extension [A: Numeric] (a: A)
    def pxToRem: Length[Double] = (Numeric[A].toDouble(a) / 192).rem
  end extension

  style(
    media.screen.maxWidth(1024.px)(unsafeRoot("html")(fontSize(102.4.px).important)),
    media.screen.minWidth(1920.px)(unsafeRoot("html")(fontSize(192.px).important)),
    media.screen.maxWidth(1600.px)(
      unsafeRoot(".top span")(transform := "scale(0.9)"),
      unsafeRoot(".top .province ul.sup li")(padding(10.pxToRem, 12.pxToRem)),
      unsafeRoot(".top .province ul.sub li")(padding(5.pxToRem, 12.pxToRem)),
      unsafeRoot(".quarter span")(transform := "scale(0.9)")
    )
  )
