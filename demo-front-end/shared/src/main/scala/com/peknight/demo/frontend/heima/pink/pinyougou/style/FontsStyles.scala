package com.peknight.demo.frontend.heima.pink.pinyougou.style

import scalacss.ProdDefaults.*
import scalacss.internal.FontFace

object FontsStyles extends StyleSheet.Inline:

  import dsl.*

  // 声明字体图标 这里一定要注意路径的变化（独立的css文件时）
  val icomoon: FontFace[String] = fontFace("icomoon")(_
    .src(
      // "url('/fonts/icomoon.eot?7gw1oq')",
      "url('/fonts/icomoon.eot?7gw1oq#iefix') format('embedded-opentype')",
      "url('/fonts/icomoon.ttf?7gw1oq') format('truetype')",
      "url('/fonts/icomoon.woff?7gw1oq') format('woff')",
      "url('/fonts/icomoon.svg?7gw1oq#icomoon') format('svg')"
    )
    .fontWeight.normal
    .fontStyle.normal
  )


