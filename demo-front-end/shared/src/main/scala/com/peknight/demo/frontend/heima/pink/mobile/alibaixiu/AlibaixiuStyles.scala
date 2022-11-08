package com.peknight.demo.frontend.heima.pink.mobile.alibaixiu

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

object AlibaixiuStyles extends StyleSheet.Standalone:
  import dsl.*

  "ul" - (
    listStyle := "none",
    margin.`0`,
    padding.`0`,
  )

  "a" - (
    color(c"#666"),
    textDecoration := "none",
    &.hover - (
      textDecoration := "none",
    ),
  )

  "body" - (
    backgroundColor(c"#f5f5f5"),
  )

  ".container" - (
    backgroundColor.white,
  )

  // header
  "header" - (
    paddingLeft.`0`.important,
  )

  ".logo" - (
    backgroundColor(c"#429ad9"),
    textAlign.center,
    unsafeChild("img")(
      display.block,
      // 图片自己的宽度 logo图片不需要缩放
      maxWidth(100.%%),
      margin(`0`, auto),
    ),
    unsafeChild("span")(
      display.block,
      height(50.px),
      lineHeight(50.px),
      color.white,
      fontSize(18.px),
    )
  )

  // 我们如果进入了超小屏幕下 logo里面的图片就隐藏起来
  // 我们事先准备好一个盒子在logo里面，它平时是隐藏起来的，只有在超小屏幕下显示

  ".nav" - (
    backgroundColor(c"#eee"),
    borderBottom(1.px, solid, c"#ccc"),
    unsafeChild("ul")(
      width(100.%%),
      unsafeChild("li")(width(100.%%)),
    ),
    unsafeChild("a")(
      display.block,
      width(100.%%),
      height(50.px),
      lineHeight(50.px),
      paddingLeft(30.px),
      fontSize(16.px),
      &.hover - (
        backgroundColor.white,
        color(c"#333"),
      ),
      &.before - (
        paddingRight(5.px),
      ),
    ),
  )

  ".news ul li" - (
    float.left,
    width(25.%%),
    height(128.px),
    paddingRight(10.px),
    marginBottom(10.px),
    unsafeChild("a")(
      display.block,
      position.relative,
      width(100.%%),
      height(100.%%),
    ),
    unsafeChild("img")(
      width(100.%%),
      height(100.%%),
    ),
    &.firstOfType - (
      width(50.%%),
      height(266.px),
      unsafeChild(".carousel-caption")(
        lineHeight(41.px),
        fontSize(20.px),
        padding(`0`, 10.px),
      ),
      unsafeChild("img")(
        height(266.px)
      )
    ),
  )

  ".news ul li a p, .carousel-caption" - (
    position.absolute,
    bottom.`0`,
    left.`0`,
    width(100.%%),
    height(41.px),
    padding(5.px, 10.px),
    marginBottom.`0`,
    background := rgba(0, 0, 0, .5),
    fontSize(12.px),
    color.white,
  )

  ".publish" - (
    borderTop(1.px, solid, c"#ccc"),
  )

  ".publish .row" - (
    borderBottom(1.px, solid, c"#ccc"),
    padding(10.px, `0`),
  )

  ".pic" - (
    marginTop(10.px),
  )

  ".pic img" - (
    width(100.%%),
  )

  ".banner img" - (
    width(100.%%),
  )

  ".hot" - (
    display.block,
    marginTop(20.px),
    padding(`0`, 20.px, 20.px),
    border(1.px, solid, c"#ccc"),
  )

  ".hot span" - (
    borderRadius.`0`,
    marginBottom(20.px),
  )

  ".hot p" - (
    fontSize(12.px),
  )