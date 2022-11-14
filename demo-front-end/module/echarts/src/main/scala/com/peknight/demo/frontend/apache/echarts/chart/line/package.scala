package com.peknight.demo.frontend.apache.echarts.chart

import com.peknight.demo.frontend.apache.echarts.util.OptionDataValue

import scala.scalajs.js

package object line:
  type LineDataValue = OptionDataValue | js.Array[OptionDataValue]
