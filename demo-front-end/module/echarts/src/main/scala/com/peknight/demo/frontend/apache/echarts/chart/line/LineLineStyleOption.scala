package com.peknight.demo.frontend.apache.echarts.chart.line

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.{LineStyleOption, ZRColor}

import scala.scalajs.js

trait LineLineStyleOption extends LineStyleOption[ZRColor]:
  type WidthType = Number | "bolder"
