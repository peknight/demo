package com.peknight.demo.frontend.heima.pink.pinyougou.style

import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*

object PinyougouStyles extends StyleSheet.Standalone:

  import dsl.*

  "*" - (
    margin.`0`,
    padding.`0`,
    color(black),
    fontSize(12.px),
    fontWeight._400,
    boxSizing.borderBox
  )

  "input".focus - outline.none

  ".white_mask" - (
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

  ".w" - (
    width(1200.px),
    height(100.%%),
    margin(`0`, auto)
  )

  "header" - borderBottom(2.px, solid, c"#bb0000")

  ".top_bar" - (
    height(32.px),
    backgroundColor(c"#eee"),
  )

  ".top_bar .welcome" - (
    float.left,
    lineHeight(32.px),
  )

  ".top_bar .welcome .login, .top_bar .welcome .register" - color(c"#50c1ef")

  ".top_bar .top_nav" - float.right

  ".top_bar .top_nav ul li" - float.left

  ".top_bar .top_nav ul li a" - (
    display.block,
    margin(8.px, 0.px),
    padding(0.px, 8.px, 0.px),
    borderRight(1.px, solid, black)
  )

  ".top_bar .top_nav ul li:last-child a" - style(borderRight :=! "0px none black")

  ".top_bar .top_nav ul .top_menu a".after - (
    fontFamily :=! "'icomoon'",
    content :=! "'\\e900'"
  )

  ".logo_search_cart_area" - height(105.px)

  ".logo_search_cart_area .logo_area" - (
    float.left,
    width(210.px)
  )

  ".logo_search_cart_area .logo_area .logo_pic" - (
    display.block,
    width(175.px),
    height(55.px),
    margin(25.px, 17.px),
    background := "url('images/logo.png'), no-repeat"
  )

  ".logo_search_cart_area .search_area" - (
    float.left,
    marginTop(30.px),
    marginLeft(100.px)
  )

  ".logo_search_cart_area .search_area .search_bar input" - (
    float.left,
    width(500.px),
    height(30.px),
    border(2.px, solid, c"#b00")
  )

  ".logo_search_cart_area .search_area .search_bar button" - (
    float.left,
    width(70.px),
    height(30.px),
    border(2.px, solid, c"#b00"),
    backgroundColor(c"#b00"),
    color(white),
    fontSize(13.px)
  )

  ".logo_search_cart_area .search_area .search_nav" - marginTop(10.px)

  ".logo_search_cart_area .search_area .search_nav li" - (
    float.left,
    marginRight(25.px)
  )

  ".logo_search_cart_area .search_area .search_nav li".firstChild - color(red)

  ".logo_search_cart_area .cart_area button" - (
    position.relative,
    width(145.px),
    height(30.px),
    marginTop(30.px),
    marginLeft(155.px),
    border(1.px, solid, c"#ccc"),
    backgroundColor(c"#eee")
  )

  ".logo_search_cart_area .cart_area button .cart_icon" - (
    position.absolute,
    left(10.px),
    top(4.px),
    width(25.px),
    height(20.px),
    background := "url('images/icons.png') no-repeat",
    backgroundPosition := "-413px -86px"
  )

  ".logo_search_cart_area .cart_area button .badge" - (
    position.absolute,
    top(-7.px),
    right(20.px),
    width(14.px),
    height(14.px),
    borderRadius(7.px),
    backgroundColor(red),
    color(white),
    fontSize(10.px),
    lineHeight(16.px)
  )

  ".head_nav_area" - height(45.px)

  ".head_nav_area .category_head" - (
    float.left,
    width(210.px),
    backgroundColor(c"#b00"),
    color(white),
    fontSize(15.px),
    lineHeight(45.px),
    textAlign.center
  )

  ".head_nav_area .head_nav ul li" - (
    float.left,
    margin(0.px, 22.px),
    fontSize(15.px),
    lineHeight(45.px)
  )

  ".top_container" - height(460.px)

  ".top_container .item_category" - (
    float.left,
    width(210.px),
    height(100.%%),
    backgroundColor(c"#c50000")
  )

  ".top_container .item_category li a" - (
    display.block,
    height(30.px),
    paddingLeft(18.px),
    color(white),
    fontSize(14.px),
    lineHeight(30.px)
  )

  ".top_container .item_category li a".after - (
    float.right,
    marginRight(15.px),
    fontFamily :=! "icomoon",
    content :=! "'\\e901'"
  )

  ".top_container .banner_area" - (
    float.left,
    width(730.px),
    height(454.px),
    marginLeft(5.px),
    marginTop(5.px)
  )

  ".top_container .banner_area img" - (
    width(100.%%),
    height(100.%%)
  )

  ".top_container .banner_area .prev, .top_container .banner_area .next" - (
    position.absolute,
    top(50.%%),
    height(50.px),
    marginTop(-25.px),
    background := rgba(0, 0, 0, .3),
    color(white),
    fontSize(30.px),
    lineHeight(50.px),
    fontFamily :=! "icomoon"
  )

  ".top_container .banner_area .prev" - (
    left(0.px),
    borderTopRightRadius(25.px),
    borderBottomRightRadius(25.px),
  )

  ".top_container .banner_area .next" - (
    right(0.px),
    borderTopLeftRadius(25.px),
    borderBottomLeftRadius(25.px)
  )

  ".top_container .banner_area .banner_nav" - (
    position.absolute,
    left(50.%%),
    bottom(12.px),
    marginLeft(-28.px)
  )

  ".top_container .banner_area .banner_nav li" - (
    float.left,
    width(10.px),
    height(10.px),
    margin(0.px, 2.px),
    borderRadius(5.px),
    background := rgba(255, 255, 255, .5)
  )

  ".top_container .banner_area .banner_nav li".firstOfType - style(background := rgba(255, 255, 255, 1))

  ".top_container .news_nav_banner_area" - (
    float.left,
    width(248.px),
    marginLeft(5.px),
    marginTop(5.px)
  )

  ".top_container .news_nav_banner_area .news_area" - (
    width(100.%%),
    height(180.px),
    paddingLeft(5.px),
    paddingRight(5.px),
    border(1.px, solid, c"#ccc")
  )
  ".top_container .news_nav_banner_area .news_area .news_header" - (
    height(40.px),
    paddingLeft(10.px),
    paddingRight(10.px),
    borderBottom(1.px, solid, c"#ccc")
  )
  ".top_container .news_nav_banner_area .news_area .news_header h4" - (
    float.left,
    fontSize(14.px),
    lineHeight(40.px)
  )
  ".top_container .news_nav_banner_area .news_area .news_header a" - (
    float.right,
    lineHeight(40.px),

    &.after - (
      fontFamily :=! "'icomoon'",
      content :=! "'\\e901'"
    )
  )

  ".top_container .news_nav_banner_area .news_area .news_body" - (
    paddingLeft(5.px),
    paddingRight(5.px),
    paddingTop(7.px)
  )

  ".top_container .news_nav_banner_area .news_area .news_body li" - (
    lineHeight(25.px),
    whiteSpace.nowrap
  )

  ".top_container .news_nav_banner_area .nav_area" - width(100.%%)

  ".top_container .news_nav_banner_area .nav_area .nav_line" - (
    width(100.%%),
    background := "url('images/icons.png') center no-repeat",
  )

  ".top_container .news_nav_banner_area .nav_area .nav_line1" - style(backgroundPosition := "3px -7px")
  ".top_container .news_nav_banner_area .nav_area .nav_line2" - style(backgroundPosition := "3px -75px")
  ".top_container .news_nav_banner_area .nav_area .nav_line3" - style(backgroundPosition := "3px -148px")

  ".top_container .news_nav_banner_area .nav_area li" - (
    float.left,
    width(62.px),
    height(65.px),
    borderLeft(1.px, solid, c"#ccc"),
    borderBottom(1.px, solid, c"#ccc"),
    textAlign.center,
    lineHeight(100.px)
  )

  ".top_container .news_nav_banner_area .nav_area li:nth-of-type(4n)" - borderRight(1.px, solid, c"#ccc")

  ".top_container .news_nav_banner_area .little_banner_area" - (
    width(100.%%),
    height(75.px),
    marginTop(5.px)
  )

  ".top_container .news_nav_banner_area .little_banner_area img" - (
    width(100.%%),
    height(100.%%)
  )

  ".middle_banner" - (
    height(163.px),
    marginTop(12.px)
  )

  ".middle_banner dl" - height(100.%%)

  ".middle_banner dl dt" - (
    float.left,
    overflow.hidden,
    width(204.px),
    height(100.%%),
    backgroundColor(c"#444")
  )

  ".middle_banner dl dt img" - (
    display.block,
    margin(30.px, auto, 12.px)
  )

  ".middle_banner dl dt h4" - (
    color(white),
    fontSize(18.px),
    fontWeight._600,
    textAlign.center
  )

  ".middle_banner dl dd" - float.left

  ".recommend_area" - marginTop(25.px)

  ".recommend_area .recommend_head h2" - (
    float.left,
    fontSize(20.px),
    fontWeight._500
  )

  ".recommend_area .recommend_head .change" - (
    float.right,
    width(66.px),
    height(25.px),
    border :=! "0px none",
    background := "rgba(255, 255, 255, 0) url('images/icons.png')",
    backgroundPosition := "-380px -105px",
    color(c"#50c1ef"),
    textAlign.left
  )

  ".recommend_area ul" - border(1.px, solid, c"#ccc")

  ".recommend_area ul .recommend_item" - (
    float.left,
    overflow.auto,
    width(200.px),
    marginTop(18.px),
    marginBottom(18.px),
    paddingLeft(29.px),
    paddingRight(29.px),
    borderRight(1.px, solid, c"#ccc")
  )
  ".recommend_area ul li:first-of-type" - width(199.px)

  ".recommend_area ul li:last-of-type" - (
    width(199.px),
    borderRight :=! "0px none"
  )
  ".recommend_area ul .recommend_item .item_pic img" - (
    display.block,
    width(142.px),
    height(142.px),
    margin(0.px, auto, 5.px)
  )
  ".recommend_area ul .recommend_item .item_price" - (
    display.block,
    marginTop(9.px),
    color(c"#c50000"),
    fontFamily :=! "'WenQuanYi Micro Hei Mono'",
    fontSize(20.px),
    fontWeight._700
  )
  ".sidebar_area" - (
      position.fixed,
      top(0.px),
      right(0.px),
      height(100.%%)
  )
  ".sidebar_area .right_bar" - (
    float.right,
    width(5.px),
    height(100.%%),
    backgroundColor(c"#666")
  )
  ".sidebar_area .sidebar_icon" - (
    width(100.%%),
    height(32.px),
    marginTop(2.px),
    borderRadius(4.px),
    background := "#666 url('images/cartPanelViewIcons.png')",
  )
  ".sidebar_area .middle_area" - (
    position.absolute,
    top(50.%%),
    right(0.px),
    width(32.px),
    height(100.px),
    marginTop(-50.px)
  )
  ".sidebar_area .middle_area .cart_icon" - style(backgroundPosition := "-52px -2px")
  ".sidebar_area .middle_area .favorite_icon" - style(backgroundPosition := "-52px -50px")
  ".sidebar_area .middle_area .history_icon" - style(backgroundPosition := "-52px -101px")
  ".sidebar_area .bottom_area" - (
    position.absolute,
    bottom(0.px),
    right(0.px),
    width(32.px),
    height(66.px)
  )
  ".sidebar_area .bottom_area .top_icon" - style(backgroundPosition := "-52px -252px")
  ".sidebar_area .bottom_area .comment_icon" - style(backgroundPosition := "-52px -300px")
