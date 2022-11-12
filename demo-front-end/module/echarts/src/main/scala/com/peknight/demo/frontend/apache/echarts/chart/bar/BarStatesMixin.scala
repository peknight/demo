package com.peknight.demo.frontend.apache.echarts.chart.bar

import com.peknight.demo.frontend.apache.echarts.util.{DefaultStatesMixinEmphasis, StatesMixinBase}

import scala.scalajs.js

trait BarStatesMixin extends StatesMixinBase:
  type EmphasisType = DefaultStatesMixinEmphasis
  type SelectType = js.Any
  type BlurType = js.Any
