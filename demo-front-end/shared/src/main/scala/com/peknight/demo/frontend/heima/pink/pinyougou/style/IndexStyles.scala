package com.peknight.demo.frontend.heima.pink.pinyougou.style

import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*

object IndexStyles extends StyleSheet.Standalone:
  import dsl.*

  ".main" - (
    width(980.px),
    height(455.px),
    marginLeft(220.px),
    marginTop(10.px)
  )

  ".focus" - (
    position.relative,
    width(721.px),
    height(455.px),
    overflow.hidden
  )

  ".focus ul" - (
    position.absolute,
    top.`0`,
    left.`0`,
    width(600.%%)
  )

  ".focus ul li" - float.left

  ".focus ul li a img" - (
    width(720.px),
    height(455.px)
  )

  ".arrow-l, .arrow-r" - (
    display.none,
    position.absolute,
    top(50.%%),
    marginTop(-20.px),
    width(24.px),
    height(40.px),
    background := rgba(0, 0, 0, .3),
    textAlign.center,
    lineHeight(40.px),
    color(c"#fff"),
    fontSize(18.px),
    zIndex(2),
  )

  ".arrow-l" - left(0.px)
  ".arrow-r" - right(0.px)

  ".circle" - (
    position.absolute,
    bottom(10.px),
    left(50.%%),
    transform := "translateX(-50%)"
  )

  ".circle li" - (
    float.left,
    width(8.px),
    height(8.px),
    border(2.px, solid, rgba(255, 255, 255, .5)),
    margin(0.px, 3.px),
    borderRadius(50.%%),
    cursor.pointer,
  )

  ".current" - backgroundColor(c"#fff")

  ".newsflash" - (float.right, width(250.px), height(455.px))

  ".news" - (height(165.px), border(1.px, solid, c"#e4e4e4"))

  ".news-hd" - (
    height(33.px),
    lineHeight(33.px),
    borderBottom(1.px, dotted, c"#e4e4d4"),
    padding(`0`, 15.px)
  )

  ".news-hd h5" - (float.left, fontSize(14.px))

  ".news-hd .more" - (
    float.right,
    &.after - (content.string("\ue902"), fontFamily(FontsStyles.icomoon))
  )

  ".news-bd" - padding(5.px, 15.px, `0`)

  ".news-bd ul li" - (
    height(24.px),
    lineHeight(24.px),
    // 文本超长省略号处理
    overflow.hidden,
    whiteSpace.nowrap,
    textOverflow := "ellipsis",
  )

  ".lifeservice" - (
    overflow.hidden,
    height(209.px),
    border(1.px, solid, c"#e4e4e4"),
    borderTop.`0`
  )

  ".lifeservice ul" - width(252.px)

  ".lifeservice ul li" - (
    position.relative,
    float.left,
    width(63.px),
    height(71.px),
    borderRight(1.px, solid, c"#e4e4e4"),
    borderBottom(1.px, solid, c"#e4e4e4"),
    textAlign.center
  )

  ".lifeservice ul li i" - (
    display.inlineBlock,
    width(24.px),
    height(28.px),
    marginTop(12.px),
    background := "url('/images/icons.png') no-repeat"
  )

  ".lifeservice ul li .promotion" - (
    position.absolute,
    top.`0`,
    right.`0`,
    width(14.px),
    height(16.px),
    fontSize(12.px),
    color(white),
    backgroundColor(green),
    &.after - (
      display.block,
      position.absolute,
      top(100.%%),
      right.`0`,
      borderLeft(7.px, solid, green),
      borderRight(7.px, solid, green),
      borderBottom(5.px, solid, transparent),
      content.string(""),
    )
  )

  ".lifeservice ul li:nth-of-type(1) i" - style(backgroundPosition := "-19px -15px")
  ".lifeservice ul li:nth-of-type(2) i" - style(backgroundPosition := "-79px -17px")
  ".lifeservice ul li:nth-of-type(3) i" - style(backgroundPosition := "-142px -17px")
  ".lifeservice ul li:nth-of-type(4) i" - style(backgroundPosition := "-205px -16px")
  ".lifeservice ul li:nth-of-type(5) i" - style(backgroundPosition := "-17px -85px")
  ".lifeservice ul li:nth-of-type(6) i" - style(backgroundPosition := "-79px -87px")
  ".lifeservice ul li:nth-of-type(7) i" - style(backgroundPosition := "-142px -87px")
  ".lifeservice ul li:nth-of-type(8) i" - style(backgroundPosition := "-206px -86px")
  ".lifeservice ul li:nth-of-type(9) i" - style(backgroundPosition := "-17px -155px")
  ".lifeservice ul li:nth-of-type(10) i" - style(backgroundPosition := "-79px -157px")
  ".lifeservice ul li:nth-of-type(11) i" - style(backgroundPosition := "-142px -152px")
  ".lifeservice ul li:nth-of-type(12) i" - style(backgroundPosition := "-205px -156px")

  ".bargain" - marginTop(6.px)

  // 推荐模块
  ".recom" - (height(163.px), marginTop(12.px), backgroundColor(c"#ebebeb"))

  ".recom-hd" - (
    float.left,
    width(205.px),
    height(100.%%),
    paddingTop(30.px),
    backgroundColor(c"#5c5251"),
    textAlign.center
  )

  ".recom-hd h4" - (color(white), fontSize(18.px), marginTop(12.px))

  ".recom-bd" - float.left

  ".recom-bd ul li" - (position.relative, float.left)

  ".recom-bd ul li img" - (width(248.px), height(163.px))

  ".recom-bd ul li".nthChild("-n+3").after - (
    content.string(""),
    position.absolute,
    right.`0`,
    top(10.px),
    width(1.px),
    height(145.px),
    backgroundColor(c"#ddd")
  )

  // 猜你喜欢模块
  ".like" - marginTop(25.px)

  ".like-hd" - height(48.px)

  ".like-hd h3" - (fontSize(20.px), fontWeight._500)

  ".like-hd .change-btn" - (
    height(25.px),
    lineHeight(25.px),
    &.after - (
      content.string(""),
      display.block,
      float.right,
      height(100.%%),
      width(20.px),
      marginLeft(2.px),
      backgroundColor(pink),
      background := "url(/images/icons.png) no-repeat -418px -105px",
    )
  )

  ".like-hd .change-btn a" - color(c"#50c1ef")

  ".like-bd" - (height(250.px), border(1.px, solid, c"#e4e4e4"))

  ".like-bd ul" - textAlign.center

  ".like-bd ul li" - (
    position.relative,
    float.left,
    width(200.px),
    height(248.px),
    paddingTop(18.px),
    &.firstOfType - width(199.px),
    &.lastOfType - width(199.px)
  )

  ".like-bd ul li".nthChild("-n+5").after - (
    content.string(""),
    position.absolute,
    right.`0`,
    top(10.px),
    width(1.px),
    height(228.px),
    backgroundColor(c"#e4e4e4")
  )

  ".like-bd ul li .like-text" - (padding(`0`, 25.px), textAlign.left)

  ".like-bd ul li .item-title" - fontSize(14.px)

  ".like-bd ul li .item-price" - (
    marginTop(9.px),
    fontSize(20.px),
    fontWeight._700,
    color(c"#b1191a"),
    fontFamily :=! "'WenQuanYi Micro Hei Mono'",
  )

  // 家用电器模块

  ".box-hd" - (
    height(30.px),
    borderBottom(2.px, solid, c"#c81623")
  )

  ".box-hd h3" - (
    float.left,
    fontSize(18.px),
    color(c"#c81623"),
    fontWeight._400,
  )

  ".tab-list" - (
    float.right,
    lineHeight(30.px),
  )

  ".tab-list ul li" - (
    float.left,
    &.after - style(content.string("|")),
    &.lastOfType.after - style(content.string(""))
  )

  ".tab-list ul li a" - margin(`0`, 15.px)

  ".floor .w" - (
    marginTop(30.px),
    borderBottom(1.px, solid, c"#ccc")
  )

  ".box-bd" - (
    height(378.px),
    paddingBottom(17.px),
  )

  // 如果用inline引入这里的`>`会被UrlEncode导致样式失效，导出文件后引入没有这个问题
  ".tab-list-item > div" - (
    float.left,
    height(100.%%)
  )

  ".col-210" - (
    width(210.px),
    backgroundColor(c"#f9f9f9"),
    textAlign.center,
  )

  ".col-210 ul" - (
    paddingLeft(12.px),
  )

  ".col-210 ul li" - (
    float.left,
    width(85.px),
    height(34.px),
    borderBottom(1.px, solid, c"#ccc"),
    textAlign.center,
    // 去掉1px边框
    lineHeight(33.px),
    marginRight(10.px),
  )

  ".col-219" - width(219.px)
  ".col-221" - (
    width(221.px),
    borderRight(1.px, solid, c"#ccc")
  )

  ".col-329" - width(329.px)

  ".bb" - (
    // 一般情况下，a如果包含有宽度的盒子，a需要转换为块级元素
    display.block,
    borderBottom(1.px, solid, c"#ccc")
  )

  ".brand" - (
    height(67.px),
    margin(10.px, `0`),
    backgroundColor(c"#f7f7f7")
  )

  ".brand ul" - (
    height(100.%%),
    padding(15.px, `0`)
  )

  ".brand ul li" - (
    float.left,
    borderRight(1.px, dotted, c"#e4e4e4"),
    padding(`0`, 10.px),
    &.lastOfType - style(borderRight.`0`)
  )

  ".sidebar" - (
    position.fixed,
    top.`0`,
    right.`0`,
    height(100.%%)
  )

  ".sidebar .right-bar" - (
    float.right,
    width(5.px),
    height(100.%%),
    backgroundColor(c"#7a6e6e")
  )

  ".sidebar .sidebar-icon" - (
    position.relative,
    width(100.%%),
    height(32.px),
    marginTop(2.px),
    borderRadius(4.px),
    background := "#7a6e6e url('/images/cartPanelViewIcons.png')",
  )

  ".sidebar .middle" - (
    position.absolute,
    top(50.%%),
    right.`0`,
    width(32.px),
    height(100.px),
    marginTop(-50.px)
  )

  ".sidebar .middle .cart-icon " - style(backgroundPosition := "-51px -2px")
  ".sidebar .middle .favorite-icon" - style(backgroundPosition := "-51px -50px")
  ".sidebar .middle .history-icon" - style(backgroundPosition := "-51px -101px")

  ".sidebar .middle .cart-icon .badge" - (
    position.absolute,
    top(-5.px),
    left(16.px),
    minWidth(12.px),
    fontSize(10.px),
    lineHeight(10.px),
    borderRadius(6.px),
    color(c"#fff"),
    backgroundColor(c"#e60012"),
    textAlign.center
  )

  ".sidebar .bottom" - (
    position.absolute,
    bottom.`0`,
    right.`0`,
    width(32.px),
    height(66.px)
  )

  ".sidebar .bottom .top-icon" - style(backgroundPosition := "-51px -252px")
  ".sidebar .bottom .comment-icon" - style(backgroundPosition := "-51px -300px")

  ".fixed-tool" - (
    position.fixed,
    top(100.px),
    left(50.%%),
    marginLeft(-676.px),
    width(66.px),
    backgroundColor(c"#fff"),
    display.none
  )

  ".fixed-tool li" - (
    height(32.px),
    lineHeight(32.px),
    textAlign.center,
    fontSize(12.px),
    borderBottom(1.px, solid, c"#ccc"),
    cursor.pointer,
  )

  ".fixed-tool .current" - (
    backgroundColor(c"#c81623"),
    color(c"#fff")
  )