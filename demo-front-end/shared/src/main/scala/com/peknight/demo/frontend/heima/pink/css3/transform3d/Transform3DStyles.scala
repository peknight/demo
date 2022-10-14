package com.peknight.demo.frontend.heima.pink.css3.transform3d

import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*

/**
 * translateZ
 * - 沿Z轴移动
 * - 后面的单位我们一般跟px
 * - translateZ(100px) 向外移动100px (向我们的眼睛来移动的)
 * - 3D移动有简写的方法 translate3d 里面的三个参数 x y z 是不能省略的，没有就写0
 */
object Transform3DStyles extends StyleSheet.Standalone:
  import dsl.*

  "*" - (
    margin.`0`,
    padding.`0`,
    color.white,
    border(1.px, solid, black),
  )

  ".box" - (
    float.left,
    perspective(500.px),
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
    &.hover - style(transform := "translateX(100px) translateY(50px) translateZ(100px)")
  )

  ".trans-2" - (
    width(200.px),
    height(200.px),
    backgroundColor.skyblue,
    transition := "all 5s",
    &.hover - style(transform := "translate3d(100px,50px,-100px)")
  )

  "img".attrStartsWith("class", "rotate-1") - (
    display.block,
    width(200.px),
    height(200.px),
    transition := "all 5s",
  )

  ".rotate-1-x".hover - style(transform := "rotateX(360deg)")

  ".rotate-1-y".hover - style(transform := "rotateY(360deg)")

  ".rotate-1-z".hover - style(transform := "rotateZ(360deg)")

  ".rotate-1-3d".hover - style(transform := "rotate3d(1,1,0,360deg)")

  ".style-1-outer" - (
    position.relative,
    width(200.px),
    height(200.px),
    transition := "all 2s",
    // 保持子元素3D立体空间，默认是flat 子元素不开启3D立体空间
    transformStyle.preserve3D,
    &.hover - style(transform := "rotateY(60deg)")
  )

  ".style-1-outer .style-1-inner" - (
    position.absolute,
    top.`0`,
    left.`0`,
    width(100.%%),
    height(100.%%),
    &.firstOfType - backgroundColor(indigo),
    &.lastOfType - (
      backgroundColor(rosybrown),
      transform := "rotateX(60deg)",
    )
  )

  ".case-1-outer" - (
    position.relative,
    width(200.px),
    height(200.px),
    transition := "all 5s",
    transformStyle.preserve3D,
    &.hover - (
      transform := "rotateY(180deg)"
    )
  )

  ".case-1-outer div" - (
    position.absolute,
    top.`0`,
    left.`0`,
    width(100.%%),
    height(100.%%),
    borderRadius(50.%%),
    fontSize(30.px),
    textAlign.center,
    lineHeight(200.px),
  )

  ".case-1-front" - (
    backgroundColor.pink,
    // 浏览器不显示背面，那就让前面盒子的往前走走
    transform := "translateZ(1px)"
  )

  ".case-1-back" - (
    backgroundColor.skyblue,
    transform := "rotateY(180deg)"
  )

  ".case-2 li" - (
    float.left,
    margin(`0`, 5.px),
    width(120.px),
    height(35.px),
    listStyle := "none",
    perspective(500.px),
  )

  ".case-2-outer" - (
    position.relative,
    width(100.%%),
    height(100.%%),
    transition := "all 1s",
    transformStyle.preserve3D,
    transform := "translateZ(-17.5px)",
    &.hover - (
      transform := "translateZ(-17.5px) rotateX(90deg)",
    )
  )

  ".case-2-outer div" - (
    position.absolute,
    left.`0`,
    top.`0`,
    width(100.%%),
    height(100.%%),
    fontSize(16.px),
    textAlign.center,
    lineHeight(35.px),
  )

  ".case-2-front" - (
    backgroundColor.gold,
    transform := "translateZ(17.5px)"
  )

  ".case-2-bottom" - (
    backgroundColor.brown,
    transform := "translateY(17.5px) rotateX(-90deg)",
  )

  ".case-3 li" - (
    float.left,
    margin(`0`, 5.px),
    width(120.px),
    height(35.px),
    listStyle := "none",
    perspective(500.px),
  )

  ".case-3-outer" - (
    position.relative,
    width(100.%%),
    height(100.%%),
    transition := "all 1s",
    transformStyle.preserve3D,
    &.hover - (
      transformOrigin := "center center -17.5px",
      transform := "rotateX(90deg)",
    )
  )

  ".case-3-outer div" - (
    position.absolute,
    left.`0`,
    top.`0`,
    width(100.%%),
    height(100.%%),
    fontSize(16.px),
    textAlign.center,
    lineHeight(35.px),
  )

  ".case-3-front" - (
    backgroundColor.green,
  )

  ".case-3-bottom" - (
    backgroundColor.red,
    transformOrigin := "center center -17.5px",
    transform := "rotateX(-90deg)",
  )

  ".case-4-outer" - (
    width(1200.px),
    height(400.px),
    perspective(3000.px),
  )

  ".case-4-inner" - (
    position.relative,
    margin(100.px, auto),
    width(300.px),
    height(200.px),
    transformStyle.preserve3D,
    animation := s"${Transform3DKeyFrameStyles.rotate.name.value} 10s linear infinite",
    background := "url('/media/pig.jpg') no-repeat",
    &.hover - animationPlayState.paused,
  )

  ".case-4-inner div" - (
    position.absolute,
    top.`0`,
    left.`0`,
    width(100.%%),
    height(100.%%),
    background := "url('/media/dog.jpg') no-repeat",
    &.nthOfType(1) - (
      transform := "translateZ(500px)",
    ),
    &.nthOfType(2) - (
      transform := "rotateY(60deg) translateZ(500px)",
    ),
    &.nthOfType(3) - (
      transform := "rotateY(120deg) translateZ(500px)",
    ),
    &.nthOfType(4) - (
      transform := "rotateY(180deg) translateZ(500px)",
    ),
    &.nthOfType(5) - (
      transform := "rotateY(240deg) translateZ(500px)",
    ),
    &.nthOfType(6) - (
      transform := "rotateY(300deg) translateZ(500px)",
    ),
  )