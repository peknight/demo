package com.peknight.demo.frontend.heima.pink.pinyougou.style


import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*

/**
 * 列表页专有的css
 */
object ListStyles extends StyleSheet.Standalone:
  import dsl.*

  ".sk" - (
    position.absolute,
    left(190.px),
    top(40.px),
    padding(3.px, `0`, `0`, 14.px),
    borderLeft(1.px, solid, c"#c81523"),
  )

  ".sk-list" - float.left

  ".sk-list ul li" - float.left

  ".sk-list ul li a" - (
    display.block,
    lineHeight(47.px),
    padding(`0`, 30.px),
    fontSize(16.px),
    fontWeight._700,
    color(black),
  )

  ".sk-con" - float.left

  ".sk-con ul li" - float.left

  ".sk-con ul li a" - (
    display.block,
    lineHeight(49.px),
    padding(`0`, 20.px),
    fontSize(14.px)
  )

  ".sk-con ul li:last-of-type a".after - (content :=! "'\\e900'", fontFamily :=! "'icomoon'")
