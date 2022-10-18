package com.peknight.demo.frontend.heima.pink.mobile.ctrip

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

object CtripStyles extends StyleSheet.Standalone:
  import dsl.*

  "body" - (
    maxWidth(540.px),
    minWidth(320.px),
    margin(`0`, auto),
    font := "normal 14px/1.5 Tahoma, \"Lucida Grande\",Verdana,\"Microsoft Yahei\",STXihei,hei",
    color(c"#000"),
    background := "#f2f2f2",
    // 水平滚动条
    overflowX.hidden,
    Attr.real("-webkit-tap-highlight-color") := "transparent",
  )

  "ul" - (
    listStyle := "none",
    margin.`0`,
    padding.`0`,
  )

  "a" - (
    textDecoration := "none",
    color(c"#222"),
  )

  "div" - boxSizing.borderBox

  // 搜索模块
  ".search-index" - (
    display.flex,
    // 固定定位跟低级没有关系，它以屏幕为准
    position.fixed,
    top.`0`,
    // 固定定位没办法用margin: 0 auto;来居中
    left(50.%%),
    transform := "translateX(-50%)",
    // 固定的盒子应该有宽度
    width(100.%%),
    minWidth(320.px),
    maxWidth(540.px),
    height(44.px),
    backgroundColor(c"#f6f6f6"),
    borderTop(1.px, solid, c"#ccc"),
    borderBottom(1.px, solid, c"#ccc"),
  )

  ".search" - (
    position.relative,
    height(26.px),
    lineHeight(24.px),
    border(1.px, solid, c"#ccc"),
    flex := "1",
    fontSize(12.px),
    color(c"#666"),
    margin(7.px, 10.px),
    paddingLeft(25.px),
    borderRadius(5.px),
    boxShadow := "0 2px 4px rgba(0,0,0,.2)",
    &.before - (
      content.string(""),
      position.absolute,
      top(5.px),
      left(5.px),
      width(15.px),
      height(15.px),
      background := "url('/flexlayout/images/sprite.png') no-repeat -59px -279px",
      backgroundSize := "104px",
    ),
  )

  ".user" - (
    width(44.px),
    height(44.px),
    fontSize(12.px),
    textAlign.center,
    color(c"#2eaae0"),
    &.before - (
      content.string(""),
      display.block,
      width(23.px),
      height(23.px),
      background := "url('/flexlayout/images/sprite.png') no-repeat -59px -194px",
      backgroundSize := "104px",
      margin(4.px, auto, -2.px),
    ),
  )

  ".focus" - paddingTop(44.px)

  ".focus img" - width(100.%%)

  // 局部导航栏
  ".local-nav" - (
    display.flex,
    height(64.px),
    backgroundColor.white,
    margin(3.px, 4.px),
    borderRadius(8.px),
  )

  ".local-nav li" - (
    flex := "1",
  )

  ".local-nav a" - (
    display.flex,
    flexDirection.column,
    alignItems.center,
    fontSize(12.px),
  )

  ".local-nav li ".attrStartsWith("class", "local-nav-icon") - (
    width(32.px),
    height(32.px),
    background := "url('/flexlayout/images/localnav_bg.png') no-repeat",
    backgroundSize := "32px auto",
    marginTop(8.px),
  )

  for i <- 2 to 5 yield
    s".local-nav li .local-nav-icon-icon$i" - style(backgroundPosition := s"0 ${-32 * (i - 1)}px")

  // nav

  "nav" - (
    overflow.hidden,
    borderRadius(8.px),
    margin(`0`, 4.px, 3.px),
  )

  ".nav-common" - (
    display.flex,
    height(88.px),
    // 背景渐变必须添加浏览器私有前缀，不过scalacss都帮忙加好了，点赞
    &.nthOfType(1) - style(background := "linear-gradient(left, #fa5a55, #fa994d)"),
    &.nthOfType(2) - (
      margin(3.px, `0`),
      background := "linear-gradient(left, #4890ed, #53bced)",
    ),
    &.nthOfType(3) - style(background := "linear-gradient(left, #34c2a9, #6cd559)"),
  )

  ".nav-items" - (
    flex := "1",
    display.flex,
    flexDirection.column,
    // -n+2就是选择前面两个元素
    &.nthOfType("-n+2") - borderRight(1.px, solid, white),
  )

  ".nav-items a" - (
    flex := "1",
    textAlign.center,
    lineHeight(44.px),
    color.white,
    fontSize(14.px),
    // 文字阴影
    textShadow := "1px 1px rgba(0,0,0,.2)",
    &.firstOfType - borderBottom(1.px, solid, white),
  )

  ".nav-items:first-of-type a" - (
    border.`0`,
    background := "url('/flexlayout/images/hotel.png') no-repeat bottom center",
    backgroundSize := "121px auto",
  )

  ".subnav-entry" - (
    display.flex,
    borderRadius(8.px),
    backgroundColor.white,
    margin(`0`, 4.px),
    flexWrap.wrap,
    padding(5.px, `0`),
    boxShadow := "0 2px 4px rgba(0,0,0,.2)",
  )

  ".subnav-entry li" - (
    // 里面的子盒子可以写%，相对于父级来说的
    flex := "20%",
  )

  ".subnav-entry a" - (
    display.flex,
    flexDirection.column,
    alignItems.center,
  )

  ".subnav-entry-icon" - (
    width(28.px),
    height(28.px),
    marginTop(4.px),
    background := "url('/flexlayout/images/subnav-bg.png') no-repeat",
    backgroundSize := "28px auto",
  )

  ".subnav-entry li:nth-of-type(1) .subnav-entry-icon" - style(backgroundPosition := "0 3px")
  ".subnav-entry li:nth-of-type(2) .subnav-entry-icon" - style(backgroundPosition := "3px -28px")
  ".subnav-entry li:nth-of-type(3) .subnav-entry-icon" - style(backgroundPosition := "3px -61px")
  ".subnav-entry li:nth-of-type(4) .subnav-entry-icon" - style(backgroundPosition := "1px -320px")
  ".subnav-entry li:nth-of-type(5) .subnav-entry-icon" - style(backgroundPosition := "0 -98px")
  ".subnav-entry li:nth-of-type(6) .subnav-entry-icon" - style(backgroundPosition := "5px -161px")
  ".subnav-entry li:nth-of-type(7) .subnav-entry-icon" - style(backgroundPosition := "0 -196px")
  ".subnav-entry li:nth-of-type(8) .subnav-entry-icon" - style(backgroundPosition := "1 -130px")
  ".subnav-entry li:nth-of-type(9) .subnav-entry-icon" - style(backgroundPosition := "0 -228px")
  ".subnav-entry li:nth-of-type(10) .subnav-entry-icon" - style(backgroundPosition := "4px -286px")


  // sales-box
  ".sales-box" - (
    borderTop(1.px, solid, c"#bbb"),
    backgroundColor.white,
    margin(4.px),
  )

  ".sales-hd" - (
    position.relative,
    height(44.px),
    borderBottom(1.px, solid, c"#ccc"),
  )

  ".sales-hd h2" - (
    position.relative,
    textIndent(-9999.px),
    overflow.hidden,
    &.after - (
      position.absolute,
      top(8.px),
      left(20.px),
      content.string(""),
      width(79.px),
      height(15.px),
      background := "url('/flexlayout/images/hot.png') no-repeat 0 -20px",
      backgroundSize := "79px auto",
    )
  )

  ".more" - (
    position.absolute,
    right(5.px),
    top.`0`,
    background := "linear-gradient(left, #ff506c, #ff6bc6)",
    borderRadius(15.px),
    padding(3.px, 20.px, 3.px, 10.px),
    color.white,
    &.after - (
      content.string(""),
      position.absolute,
      top(10.px),
      right(9.px),
      width(7.px),
      height(7.px),
      borderTop(2.px, solid, white),
      borderRight(2.px, solid, white),
      transform := "rotate(45deg)"
    ),
  )

  ".row" - (
    display.flex,
  )

  ".row a" - (
    flex := "1",
    borderBottom(1.px, solid, c"#eee"),
    &.firstOfType - borderRight(1.px, solid, c"#eee"),
  )

  ".row a img" - (
    width(100.%%),
  )
