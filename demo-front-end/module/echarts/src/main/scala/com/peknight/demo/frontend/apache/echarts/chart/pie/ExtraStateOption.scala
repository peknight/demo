package com.peknight.demo.frontend.apache.echarts.chart.pie

import com.peknight.demo.frontend.apache.echarts.util.StatesMixinBase

import scala.scalajs.js

trait ExtraStateOption extends StatesMixinBase:
  type EmphasisType = EmphasisMixin
  type SelectType = js.Any
  type BlurType = js.Any
