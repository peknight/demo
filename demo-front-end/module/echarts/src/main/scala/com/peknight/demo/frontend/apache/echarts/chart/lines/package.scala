package com.peknight.demo.frontend.apache.echarts.chart

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.OptionDataValue

import scala.scalajs.js

package object lines:
  type LinesCoords = js.Array[js.Array[Number]]
  type LinesValue = OptionDataValue | js.Array[OptionDataValue]
