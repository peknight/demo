package com.peknight.demo.frontend.heima.pink.pinyougou.style


import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*

/**
 * 列表页专有的css
 */
object ListStyles extends StyleSheet.Standalone:
  import dsl.*

  // 后面的lineHeight比盒子高，这里把高的部分隐藏
  ".nav" - overflow.hidden

  ".sk" - (
    position.absolute,
    left(190.px),
    top(40.px),
    padding(3.px, `0`, `0`, 14.px),
    borderLeft(1.px, solid, c"#c81623"),
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

  ".sk-bd ul li" - (
    overflow.hidden,
    float.left,
    paddingTop(26.px),
    marginRight(13.px),
    width(290.px),
    height(460.px),
    border(1.px, solid, transparent),
    textAlign.center,
    &.nthOfType("4n") - marginRight.`0`
  )

  ".sk-bd ul li".hover - border(1.px, solid, c"#c81523")

  ".item-info" - (textAlign.left, padding(20.px, 15.px, `0`))

  ".item-info h3" - (fontSize(14.px), fontWeight._400)

  ".item-info .price" - marginTop(10.px)

  ".item-info .unit" - (fontSize(14.px), fontWeight._700)

  ".item-info .promotion-price" - (fontSize(20.px), fontWeight._700)

  ".item-info .origin-price" - (fontSize(12.px), textDecoration := "line-through")

  ".item-info .inventory" - marginTop(10.px)

  ".item-info .progress-bar" - (float.left, paddingTop(3.px))

  ".item-info .progress-bar-left" - (
    float.left,
    width(130.px),
    height(10.px),
    marginLeft(5.px),
    border(1.px, solid, c"#c81523"),
    borderRadius(5.px, `0`, `0`, 5.px),
    backgroundColor(c"#c81523"),
  )

  ".item-info .progress-bar-right" - (
    float.left,
    width(20.px),
    height(10.px),
    marginRight(5.px),
    border(1.px, solid, c"#c81523"),
    borderRadius(`0`, 5.px, 5.px, `0`),
  )

  ".sk-bd ul li .buy" - (
    display.block,
    width(100.%%),
    height(50.px),
    marginTop(10.px),
    fontSize(20.px),
    lineHeight(50.px),
    color(white),
    backgroundColor(c"#c81523")
  )
