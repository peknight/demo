package com.peknight.demo.frontend.heima.pink.pinyougou.style

import scalacss.ProdDefaults.*

object FontsStyles extends StyleSheet.Inline:

  import dsl.*

  fontFace("icomoon")(_
    .src(
      "url(fonts/icomoon.eot?qi0s96)",
      "url('fonts/icomoon.eot?qi0s9y#iefix') format('embedded-opentype')",
      "url('fonts/icomoon.ttf?qi0s9y') format('truetype')",
      "url('fonts/icomoon.woff?qi0s9y') format('woff')",
      "url('fonts/icomoon.svg?qi0s9y#icomoon') format('svg')"
    )
    .fontWeight.normal
    .fontStyle.normal
  )


