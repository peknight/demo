package com.peknight.demo.frontend.heima.pink.pinyougou.style

import scalacss.ProdDefaults.*

object FontsStyles extends StyleSheet.Inline:

  import dsl.*

  // 声明字体图标 这里一定要注意路径的变化（独立的css文件时）
  fontFace("icomoon")(_
    .src(
      "url(fonts/icomoon.eot?dk6kx9)",
      "url('fonts/icomoon.eot?dk6kx9#iefix') format('embedded-opentype')",
      "url('fonts/icomoon.ttf?dk6kx9') format('truetype')",
      "url('fonts/icomoon.woff?dk6kx9') format('woff')",
      "url('fonts/icomoon.svg?dk6kx9#icomoon') format('svg')"
    )
    .fontWeight.normal
    .fontStyle.normal
  )


