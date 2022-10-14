package com.peknight.demo.frontend.heima.pink.css3.transform3d

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
object Transform3DKeyFrameStyles extends StyleSheet.Inline:
  import dsl.*

  val rotate = keyframes(
    // 100% -> to
    100.%% -> keyframe(transform := "rotateY(360deg)")
  )
