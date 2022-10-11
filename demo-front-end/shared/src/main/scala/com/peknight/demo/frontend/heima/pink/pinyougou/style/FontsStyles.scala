package com.peknight.demo.frontend.heima.pink.pinyougou.style

import scalacss.ProdDefaults.*

object FontsStyles extends StyleSheet.Inline:

  import dsl.*

  fontFace("icomoon")(_
    .src(
      "url(fonts/icomoon.eot?3a2xdr)",
      "url('fonts/icomoon.eot?3a2xdr#iefix') format('embedded-opentype')",
      "url('fonts/icomoon.ttf?3a2xdr') format('truetype')",
      "url('fonts/icomoon.woff?3a2xdr') format('woff')",
      "url('fonts/icomoon.svg?3a2xdr#icomoon') format('svg')"
    )
    .fontWeight.normal
    .fontStyle.normal
  )


