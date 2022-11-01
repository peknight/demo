package com.peknight.demo.frontend.heima.pink.mobile.jd

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

object JingdongStyles extends StyleSheet.Standalone:
  import dsl.*

  ":root" - (
    Attr.real("--swiper-pagination-color") := c"#fff"
  )

  "body" - (
    width(100.%%),
    minWidth(320.px),
    maxWidth(640.px),
    margin(`0`, auto),
    fontSize(14.px),
    fontFamily :=! "-apple-system, Helvetica, sans-serif",
    color(c"#666"),
    lineHeight(1.5),
  )

  "*" - (
    Attr.real("-webkit-tap-highlight-color") := "transparent"
  )

  "input" - (
    Attr.real("-webkit-appearance") := "none"
  )

  "img, a" - (
    Attr.real("-webkit-touch-callout") := "none"
  )

  "a" - (
    color(c"#666"),
    textDecoration := "none",
  )

  "ul" - (
    margin.`0`,
    padding.`0`,
    listStyle := "none"
  )

  // 去除底下的空白缝隙
  "img" - verticalAlign.middle

  // css3盒子模型
  "div" - boxSizing.borderBox

  ".clearfix".after - (
    content.string(""),
    display.block,
    lineHeight.`0`,
    visibility.hidden,
    height.`0`,
    clear.both
  )

  ".open-app" - height(45.px)

  ".open-app ul li" - (
    float.left,
    height(45.px),
    lineHeight(45.px),
    backgroundColor(c"#333"),
    textAlign.center,
    color.white,
    &.nthOfType(1) - width(8.%%),
    &.nthOfType(2) - width(10.%%),
    &.nthOfType(3) - width(57.%%),
    &.nthOfType(4) - (width(25.%%), backgroundColor(c"#f63515"))
  )

  ".open-app ul li:nth-of-type(1) img" - width(10.px)

  ".open-app ul li:nth-of-type(2) img" - width(30.px)

  // 搜索
  ".search-wrap" - (
    // position.fixed,
    position :=! "-webkit-sticky",
    position.sticky,
    top.`0`,
    // 解决.search的外边距合并问题
    overflow.hidden,
    width(100.%%),
    height(44.px),
    minWidth(320.px),
    maxWidth(640.px),
    zIndex(9999)
  )

  ".search-btn" - (
    position.absolute,
    top.`0`,
    left.`0`,
    width(40.px),
    height(44.px),
    &.before - (
      content.string(""),
      display.block,
      width(20.px),
      height(18.px),
      background := "url('/jd/images/s-btn.png') no-repeat",
      backgroundSize := "20px 18px",
      margin(14.px, `0`, `0`, 15.px),
    ),
  )

  ".search-login" - (
    position.absolute,
    top.`0`,
    right.`0`,
    width(40.px),
    height(44.px),
    color.white,
    lineHeight(44.px),
  )

  ".search" - (
    position.relative,
    overflow.hidden,
    height(30.px),
    // marginTop会外边距合并，要给父盒子加overflow: hidden
    margin(7.px, 50.px, `0`),
    borderRadius(15.px),
  )

  ".jd-icon" - (
    width(20.px),
    height(15.px),
    position.absolute,
    top(8.px),
    left(13.px),
    background := "url('/jd/images/jd.png') no-repeat",
    backgroundSize := "20px 15px",
    &.after - (
      content.string(""),
      position.absolute,
      right(-8.px),
      top.`0`,
      display.block,
      width(1.px),
      height(15.px),
      backgroundColor(c"#ccc"),
    ),
  )

  ".magnifier-icon" - (
    position.absolute,
    top(8.px),
    left(50.px),
    width(18.px),
    height(15.px),
    // 二倍精灵图做法：先把大图缩放 再测量
    background := "url('/jd/images/jd-sprites.png') no-repeat -81px 0",
    // 使用的是精灵图，这样缩放会把整张精灵图缩放，不能这样搞
    // backgroundSize := "18px 15px"
    // 测量完再把精灵图缩放比例写上
    backgroundSize := "200px auto"
  )

  ".search input" - (
    border.`0`,
    width(100.%%),
    height(100.%%),
    paddingLeft(75.px),
  )

  ".slider" - marginTop(-44.px)

  ".slider img" - width(100.%%)

  ".swiper" - (width(100.%%), height(100.%%))

  ".swiper-slide" - (
    textAlign.center,
    fontSize(18.px),
    backgroundColor.white,
    display.flex,
    justifyContent.center,
    alignItems.center,
  )

  ".swiper-slide img" - (
    display.block,
    width(100.%%),
    height(100.%%),
    objectFit.cover,
  )

  // 品牌日

  ".brand" - (
    overflow.hidden,
    position.relative,
    marginTop(-10.px),
    borderRadius(10.px, 10.px, `0`, `0`),
  )

  ".brand div" - (
    float.left,
    width(33.33.%%),
    &.nthOfType(2) - width(33.34.%%)
  )

  ".brand div img" - width(100.%%)

  // nav

  "nav" - paddingTop(5.px)

  "nav a" - (float.left, width(20.%%), textAlign.center)

  "nav a img" - (width(40.px), margin(10.px, `0`))

  "nav a span" - display.block

  ".news" - marginTop(20.px)

  ".news img" - width(100.%%)

  ".news a" - (
    float.left,
    boxSizing.borderBox,
    &.nthOfType(1) - width(50.%%),
    // n+2就是从第2个往后面选
    &.nthOfType("n+2") - (width(25.%%), borderLeft(1.px, solid, c"#ccc"))
  )



