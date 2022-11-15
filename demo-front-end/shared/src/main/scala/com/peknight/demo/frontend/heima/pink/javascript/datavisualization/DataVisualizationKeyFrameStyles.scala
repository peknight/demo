package com.peknight.demo.frontend.heima.pink.javascript.datavisualization

import scalacss.ProdDefaults.*

object DataVisualizationKeyFrameStyles extends StyleSheet.Inline:
  import dsl.*

  val move = keyframes(100.%% -> keyframe(transform := "translateY(-50%)"))