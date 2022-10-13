package com.peknight.demo.frontend.heima.pink.css3.animation

import scalacss.ProdDefaults.*
import scalacss.internal.*
import scalacss.internal.Dsl.*

/**
 * 我们想页面一打开，一个盒子就从左边走到右边
 *
 * 动画序列
 * 1. 可以做多个状态的变化 keyframe关键帧
 * 2. 里面的百分比要是整数
 * 3. 里面的百分比就是总的时间的划分
 */
object AnimationKeyFrameStyles extends StyleSheet.Inline:
  import dsl.*

  val move1 = keyframes(
    // 开始状态
    // 0% -> from scalacss好像没有from和to
    0.%% -> keyframe(transform := "translateX(0px)"),
    // 100% -> to
    100.%% -> keyframe(transform := "translateX(1000px)")
  )

  val move2 = keyframes(
    // 0时不动可以不写
    0.%% -> keyframe(transform := "translate(0,0)"),
    25.%% -> keyframe(transform := "translateX(200px)"),
    50.%% -> keyframe(transform := "translate(200px,200px)"),
    75.%% -> keyframe(transform := "translateY(200px)"),
    100.%% -> keyframe(transform := "translate(0,0)")
  )

  val move3 = keyframes(
    0.%% -> keyframe(transform := "translate(0,0)"),
    100.%% -> keyframe(transform := "translateX(200px)"),
  )

  val move4 = keyframes(
    0.%% -> keyframe(transform := "translate(0,0)"),
    50.%% -> keyframe(transform := "translateX(200px)"),
    100.%% -> keyframe(transform := "translate(0,0)"),
  )
