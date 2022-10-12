package com.peknight.demo.frontend.heima.pink.pinyougou.style

import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*

object CommonStyles extends StyleSheet.Standalone:
  import dsl.*

  // 版心
  ".w" - (width(1200.px), margin(`0`, auto))

  ".fl" - float.left
  ".fr" - float.right

  ".style-red" - color(c"#c81623")

  ".white-mask" - (
    position.relative,
    &.before - (
      display.none,
      position.absolute,
      top.`0`,
      left.`0`,
      width(100.%%),
      height(100.%%),
      background := rgba(255,255,255,.3),
      content :=! "''",
      pointerEvents.none
    ),
    &.hover.before - display.block
  )

  // 快捷导航模块
  ".shortcut" - (height(31.px), lineHeight(31.px), backgroundColor(c"#f1f1f1"))

  ".shortcut ul li" - float.left

  // 选择所有的偶数的小li
  ".shortcut .fr ul li".nthChild("even") - (
    width(1.px),
    height(12.px),
    backgroundColor(c"#666"),
    margin(9.px, 15.px, `0`)
  )

  ".arrow-icon".after - (
    content :=! "'\\e900'",
    fontFamily :=! "'icomoon'",
    marginLeft(6.px)
  )

  // header 头部制作
  ".header" - (position.relative, height(105.px))

  ".logo" - (
    position.absolute,
    top(25.px),
    width(175.px),
    height(56.px)
  )

  ".logo a" - (
    display.block,
    width(175.px),
    height(56.px),
    background := "url('images/logo.png') no-repeat",
    // 京东的做法
    // fontSize(`0`),
    // 淘宝的做法让文字隐藏
    textIndent(-9999.px),
    overflow.hidden
  )

  ".search" - (
    position.absolute,
    left(346.px),
    top(25.px),
    width(538.px),
    height(36.px),
    border(2.px, solid, c"#b1191a")
  )

  ".search input" - (
    // 加浮动防止元素间的间距导致搜索按钮盒子掉下去，使用scalatags写出的页面基本不会出现这种情况
    float.left,
    width(454.px),
    height(32.px),
    paddingLeft(10.px)
  )

  ".search button" - (
    // 加浮动防止元素间的间距导致搜索按钮盒子掉下去，使用scalatags写出的页面基本不会出现这种情况
    float.left,
    width(80.px),
    height(32.px),
    backgroundColor(c"#b1191a"),
    fontSize(16.px),
    color(c"#fff")
  )

  ".hotwords" - (position.absolute, top(66.px), left(346.px))

  ".hotwords a" - margin(`0`, 10.px)

  ".shopcar" - (
    position.absolute,
    right(60.px),
    top(25.px),
    width(140.px),
    height(35.px),
    lineHeight(35.px),
    textAlign.center,
    border(1.px, solid, c"#dfdfdf"),
    backgroundColor(c"#f7f7f7"),

    &.before - (
      content :=! "'\\e904'",
      fontFamily :=! "'icomoon'",
      marginRight(5.px),
      color(c"#b1191a")
    ),

    &.after - (
      content :=! "'\\e902'",
      fontFamily :=! "'icomoon'",
      marginLeft(10.px)
    )
  )


  ".count" - (
    position.absolute,
    top(-5.px),
    // 用右对齐的话如果数量较多会往左挤，所以这里用左对齐
    left(105.px),
    height(14.px),
    lineHeight(14.px),
    color(c"#fff"),
    backgroundColor(c"#e60012"),
    padding(`0`, 5.px),
    borderRadius(7.px, 7.px, 7.px, `0`)
  )

  // nav模块制作
  ".nav" - (height(47.px), borderBottom(2.px, solid, c"#b1191a"))

  ".nav .dropdown" - (
    float.left,
    width(210.px),
    height(45.px),
    backgroundColor(c"#b1191a")
  )

  ".nav .navitems" - float.left

  ".dropdown .dt" - (
    width(100.%%),
    height(100.%%),
    color(c"#fff"),
    textAlign.center,
    lineHeight(45.px),
    fontSize(16.px)
  )

  ".dropdown .dd" - (
    width(100.%%),
    height(465.px),
    backgroundColor(c"#c81623"),
    marginTop(2.px)
  )

  ".dropdown .dd ul li" - (
    position.relative,
    height(31.px),
    lineHeight(31.px),
    marginLeft(2.px),
    paddingLeft(10.px),
    fontSize(14.px),
    color(c"#fff"),
    &.hover - (backgroundColor(c"#fff"), color(c"#c81623")),
    &.after - (
      position.absolute,
      top(1.px),
      right(10.px),
      content :=! "'\\e902'",
      fontFamily :=! "'icomoon'"
    )
  )

  ".dropdown .dd ul li a" - (fontSize(14.px), color(c"#fff"))

  ".dropdown .dd ul li:hover a" - color(c"#c81623")

  ".navitems ul li" - float.left

  ".navitems ul li a" - (
    display.block,
    height(45.px),
    lineHeight(45.px),
    fontSize(16.px),
    padding(`0`, 25.px)
  )

  // 底部模块制作
  ".footer" - (
    height(415.px),
    backgroundColor(c"#f5f5f5"),
    paddingTop(30.px)
  )

  ".mod-service" - (
    height(80.px),
    borderBottom(1.px, solid, c"#ccc")
  )

  ".mod-service ul li" - (
    float.left,
    width(240.px),
    height(50.px),
    paddingLeft(35.px)
  )

  ".mod-service ul li h5" - (
    float.left,
    width(52.px),
    height(50.px),
    background := "url('images/icons.png') no-repeat",
    marginRight(8.px)
  )

  ".mod-service .intro1 h5" - style(backgroundPosition := "-252px -2px")
  ".mod-service .intro2 h5" - style(backgroundPosition := "-254px -53px")
  ".mod-service .intro3 h5" - style(backgroundPosition := "-256px -105px")
  ".mod-service .intro4 h5" - style(backgroundPosition := "-257px -156px")
  ".mod-service .intro5 h5" - style(backgroundPosition := "-255px -209px")

  ".service-txt h4" - fontSize(14.px)

  ".service-txt p" - fontSize(12.px)

  ".mod-help" - (
    height(185.px),
    borderBottom(1.px, solid, c"#ccc"),
    paddingTop(20.px),
    paddingLeft(50.px)
  )

  ".mod-help dl" - (
    float.left,
    width(200.px),
    &.lastChild - (width(90.px), textAlign.center)
  )

  ".mod-help dl dt" - (fontSize(16.px), marginBottom(10.px))

  ".mod-copyright" - (textAlign.center, paddingTop(20.px))

  ".links" - marginBottom(15.px)

  ".links a" - margin(`0`, 3.px)

  ".copyright" - lineHeight(20.px)
