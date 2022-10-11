package com.peknight.demo.frontend.heima.pink.pinyougou.style

import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*

object IndexStyles extends StyleSheet.Standalone:
  import dsl.*
  ".main" - (
    width(980.px),
    height(455.px),
    marginLeft(220.px)
  )

  ".focus" - (
    float.left,
    width(721.px),
    height(455.px),
    backgroundColor(purple)
  )

  ".newsflash" - (
    float.right,
    width(250.px),
    height(455.px),
    backgroundColor(skyblue)
  )
