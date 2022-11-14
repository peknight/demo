package com.peknight.demo.frontend.apache.echarts.chart.line

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.ZRColor

import scala.scalajs.js

trait LineStyleOption extends com.peknight.demo.frontend.apache.echarts.util.LineStyleOption[ZRColor]:
  type WidthType = Number | "bolder"
