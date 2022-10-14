package com.peknight.demo.frontend.heima.pink.css3.animation

import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*

import scala.concurrent.duration.*

object AnimationStyles extends StyleSheet.Standalone:
  import dsl.*

  "*" - (
    margin.`0`,
    padding.`0`,
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

  // 我们想页面一打开，一个盒子就从左边走到右边
  ".ani-1" - (
    width(200.px),
    height(200.px),
    backgroundColor(pink),
    // 调用动画
    animationName(AnimationKeyFrameStyles.move1),
    // 持续时间
    animationDuration(2.second),
  )

  // 动画序列
  ".ani-2" - (
    width(200.px),
    height(200.px),
    backgroundColor(green),
    animationName(AnimationKeyFrameStyles.move2),
    animationDuration(5.seconds),
  )

  ".ani-3" - (
    width(200.px),
    height(200.px),
    backgroundColor(indigo),
    // 动画名称
    animationName(AnimationKeyFrameStyles.move3),
    // 持续时间
    animationDuration(2.seconds),
    // 运动曲线
    animationTimingFunction.linear,
    // 何时开始
    animationDelay(1.second),
    // 重复次数 无限循环：animationIterationCount.infinite
    animationIterationCount.count(3),
    // 量否反方向播放 默认：animationDirection.normal,
    animationDirection.alternate,
    // 动画结束后的状态 默认：animationFillMode.backwards返回起始状态，forwards停留在结束状态
    animationFillMode.forwards,
    &.hover - (
      // 鼠标经过停止动画，默认running
      animationPlayState.paused,
    ),
  )

  ".ani-4" - (
    width(200.px),
    height(200.px),
    backgroundColor(orange),
    // （前两个不能省略）动画名称 持续时间 运动曲线 何时开始 播放次数 是否反方向 动画起始或者结束的状态
    animation := s"${AnimationKeyFrameStyles.move4.name.value} 2s linear 1s infinite normal forwards",
    &.hover - (
      // 鼠标经过停止动画，默认running
      animationPlayState.paused,
    ),
  )

  ".map" - (
    position.relative,
    width(747.px),
    height(617.px),
    background := "#333 url('/media/map.png') no-repeat",
  )

  ".city" - (
    position.absolute,
  )

  ".beijing" - (
    top(227.px),
    right(193.px)
  )

  ".taipei" - (
    top(497.px),
    right(81.px)
  )

  ".guangzhou" - (
    top(542.px),
    right(193.px)
  )

  ".dotted" - (
    width(8.px),
    height(8.px),
    backgroundColor(c"#09f"),
    borderRadius(50.%%),
  )

  ".city div".attrStartsWith("class", "pulse") - (
    // 保证小波纹在父盒子里面水平居中 垂直居中，放大之后就会中心向四周发散
    position.absolute,
    top(50.%%),
    left(50.%%),
    transform := "translate(-50%,-50%)",
    width(8.px),
    height(8.px),
    boxShadow := "0 0 12px #009dfd",
    borderRadius(50.%%),
    animation := s"${AnimationKeyFrameStyles.pulse.name.value} 2s linear infinite"
  )

  // 提权的两种解法
  ".city div.pulse-2" - animationDelay(0.7.seconds)
  ".pulse-3" - animationDelay(1.3.seconds).important

