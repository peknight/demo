package com.peknight.demo.frontend.heima.pink.pinyougou.style

import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*

trait CopyrightStyles extends StyleSheet.Standalone:
  import dsl.*

  ".mod-copyright" - (textAlign.center, paddingTop(20.px))

  ".links" - marginBottom(15.px)

  ".links a" - margin(`0`, 3.px)

  ".copyright" - lineHeight(20.px)
