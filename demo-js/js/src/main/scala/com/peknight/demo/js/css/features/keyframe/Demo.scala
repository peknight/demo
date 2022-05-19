package com.peknight.demo.js.css.features.keyframe

import scalacss.DevDefaults.*

object Demo extends StyleSheet.Inline:

  import dsl.*

  val hello = style(height(100.px), width(30.px))

  // 用keyframe定义可以在其它地方引用，但是不会像像style方法那样生成在最终的css结果中
  val hello2 = keyframe(height(150.px), width(30.px))

  val kf1 = keyframes(
    0.%% -> hello,
    20.%% -> hello2,
    100.%% -> keyframe(height(200.px), width(60.px))
  )

  val anim1 = style(animationName(kf1))
