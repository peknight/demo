package com.peknight.demo.frontend.heima.pink.javascript.datavisualization

import scalacss.ProdDefaults.*
import scalacss.internal.{Attr, FontFace}

object DataVisualizationFontsStyles extends StyleSheet.Inline:

  import dsl.*

  fontFace("icomoon")(_
    .src(
      // "url('/echarts/fonts/icomoon.eot')",
      "url('/echarts/fonts/icomoon.eot#iefix') format('embedded-opentype')",
      "url('/echarts/fonts/icomoon.ttf') format('truetype')",
      "url('/echarts/fonts/icomoon.woff') format('woff')",
      "url('/echarts/fonts/icomoon.svg#icomoon') format('svg')"
    )
    .fontWeight.normal
    .fontStyle.normal
  )

  style(
    // 声明字体图标 这里一定要注意路径的变化（独立的css文件时）
    unsafeRoot("[class^=\"icon-\"], [class*=\" icon-\"]")(
      (fontFamily :=! "icomoon").important,
      Attr.real("speak") := "none",
      fontStyle.normal,
      fontWeight.normal,
      fontVariant := "normal",
      textTransform.none,
      lineHeight(1),
      Attr.real("-webkit-font-smoothing") := "antialiased",
      Attr.real("-moz-osx-font-smoothing") := "grayscale"
    ),
    unsafeRoot(".icon-dot:before")(content.string("\ue900")),
    unsafeRoot(".icon-cup1:before")(content.string("\ue901")),
    unsafeRoot(".icon-cup2:before")(content.string("\ue902")),
    unsafeRoot(".icon-cup3:before")(content.string("\ue903")),
    unsafeRoot(".icon-clock:before")(content.string("\ue904")),
    unsafeRoot(".icon-down:before")(content.string("\ue905")),
    unsafeRoot(".icon-cube:before")(content.string("\ue906")),
    unsafeRoot(".icon-plane:before")(content.string("\ue907")),
    unsafeRoot(".icon-train:before")(content.string("\ue908")),
    unsafeRoot(".icon-bus:before")(content.string("\ue909")),
    unsafeRoot(".icon-bag:before")(content.string("\ue90a")),
    unsafeRoot(".icon-up:before")(content.string("\ue90b"))
  )


