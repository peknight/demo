package com.peknight.demo.frontend.heima.pink.pinyougou.style

import scalacss.ProdDefaults.*

object BaseStyles extends StyleSheet.Standalone:

  import dsl.*

  "*" - (
    margin.`0`,
    padding.`0`
  )

  "em, i" - fontStyle.normal

  // "li" - listStyle := "none"