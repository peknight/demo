package com.peknight.demo.frontend.heima.pink.css3.transform2d

import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*

/**
 * 移动盒子的位置：定位、盒子的外边距、2d转换移动
 *
 * - 定义2D转换中的移动，沿着X和Y轴移动元素
 * - translate最大的优点：不会影响到其他元素的位置
 * - translate中的百分比单位是相对于自身元素的translate(50%, 50%)
 * - 对行内标签没有效果
 *
 * Scale的优势
 *
 * - 可以设置转换中心点缩放，默认是以中心点缩放的
 * - 不影响其他盒子
 */
object Transform2DStyles extends StyleSheet.Standalone:
  import dsl.*

  "*" - (
    margin.`0`,
    padding.`0`,
    border(1.px, solid, black),
    color.white,
  )

  ".box" - (
    float.left,
    &.after - style(
      visibility.hidden,
      clear.both,
      display.block,
      content.string("."),
      height.`0`
    )
  )

  ".trans-1" - (
    width(200.px),
    height(200.px),
    backgroundColor.pink,
    transition := "all 5s",
    &.hover - style(transform := "translate(100px, 50px)")
  )
  ".trans-2" - (
    width(200.px),
    height(200.px),
    backgroundColor.skyblue,
    transition := "all 5s",
    &.hover - style(transform := "translateX(50px)")
  )
  ".trans-3" - (
    width(200.px),
    height(200.px),
    backgroundColor.orange,
    transition := "all 5s",
    &.hover - style(transform := "translateY(-50%)")
  )

  ".center-outer" - (
    position.relative,
    width(200.px),
    height(200.px),
    backgroundColor.hotpink,
  )

  ".center-inner" - (
    position.absolute,
    top(50.%%),
    left(50.%%),
    width(50.px),
    height(50.px),
    backgroundColor.purple,
    transition := "all 5s",
    // 用纯百分比搞居中
    &.hover - style(transform := "translate(-50%, -50%)")
  )

  "span" - (
    backgroundColor.red,
    transition := "all 5s",
    &.hover - (transform := "translate(50px, 50px)")
  )

  ".rotate-1" - (
    width(200.px),
    height(200.px),
    backgroundColor.pink,
    transition := "all 5s",
    // 顺时针旋转360度 deg是度数
    &.hover - (transform := "rotate(360deg)")
  )

  ".triangle-1" - (
    position.relative,
    width(249.px),
    height(35.px),
    &.after - (
      content.string(""),
      position.absolute,
      top(8.px),
      right(15.px),
      width(10.px),
      height(10.px),
      // 去除上面的通用边框
      border.`0`,
      borderRight(1.px, solid, black),
      borderBottom(1.px, solid, black),
      transform := "rotate(45deg)",
      transition := "transform 2s"
    ),
    // 顺序会影响转换的效果 先旋转会改变坐标轴方向 记得要将位移放到最前
    &.hover.after - style(transform := "translate(50%,50%) rotate(225deg)")
  )

  ".origin-1" - (
    width(200.px),
    height(200.px),
    backgroundColor.skyblue,
    transition := "all 5s",
    // 变换中心位置 可以跟方位名词 默认是50% 50% 等价于center center
    transformOrigin := "left bottom",
    &.hover - (transform := "rotate(360deg)"),
  )

  ".origin-2" - (
    width(200.px),
    height(200.px),
    backgroundColor.orange,
    transition := "all 5s",
    // 变换中心位置 可以是px 像素
    transformOrigin := "50px 50px",
    &.hover - (transform := "rotate(-360deg)"),
  )

  ".case-1" - (
    overflow.hidden,
    width(200.px),
    height(200.px),
    backgroundColor.purple,
    &.before - (
      content.string("case-1-before"),
      display.block,
      width(100.%%),
      height(100.%%),
      backgroundColor.hotpink,
      transition := "transform 2s",
      transform := "rotate(90deg)",
      transformOrigin := "left bottom",
    ),
    &.hover.before - (
      transform := "rotate(0deg)",
    )
  )

  // 缩放
  ".scale-1" - (
    width(200.px),
    height(200.px),
    backgroundColor(skyblue),
    transition := "all 2s",
    // 里面写的数字不跟单位，就是倍数的意思 1 就是1倍 2就是2倍
    &.hover - style(transform := "scale(10%, .1)")
  )
  ".scale-2" - (
    width(200.px),
    height(200.px),
    backgroundColor(orange),
    transition := "all 2s",
    &.hover - style(transform := "scale(2, 0.5)")
  )
  ".scale-3" - (
    width(200.px),
    height(200.px),
    backgroundColor(pink),
    transition := "all 2s",
    // 等比例缩放 简单写法
    &.hover - (
      // 变换中心位置放在hover里面 被transition的时候也是有变化效果的
      transformOrigin := "left bottom",
      transform := "scale(2)",
    )
  )

  ".case-2" - (
    overflow.hidden,
    position.relative,
    float.left,
    width(200.px),
    height(200.px),
    backgroundColor(pink),
    margin(10.px),
  )

  ".case-2-inner" - (
    position.absolute,
    top(50.%%),
    left(50.%%),
    width(95.%%),
    height(95.%%),
    backgroundColor(skyblue),
    transition := "all 1s",
    transform := "translate(-50%,-50%)",
    &.hover - style(transform := "translate(-50%,-50%) scale(1.1)")
  )

  ".case-3-li" - (
    float.left,
    width(30.px),
    height(30.px),
    margin(10.px),
    textAlign.center,
    lineHeight(30.px),
    listStyle := "none",
    borderRadius(50.%%),
    cursor.pointer,
    backgroundColor(orange),
    transition := "all 1s",
    &.hover - style(transform := "scale(1.2)")
  )

  ".multiple-1" - (
    width(200.px),
    height(200.px),
    backgroundColor(green),
    transition := "all 5s",
    &.hover - style(transform := "translate(50px,50px) rotate(180deg) scale(.5)")
  )

  ".multiple-2" - (
    width(200.px),
    height(200.px),
    backgroundColor(indigo),
    transition := "all 5s",
    // 执行顺序问题
    &.hover - style(transform := "rotate(180deg) translate(50px,50px) scale(1.2)")
  )
