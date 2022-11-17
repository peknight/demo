package com.peknight.demo.frontend.apache.echarts.chart

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.ecomfe.zrender.core.BuiltinTextPosition

import scala.scalajs.js

package object bar:
  type PolarBarLabelPosition = BuiltinTextPosition | js.Array[Number | String] | "start" | "insideStart" | "middle" | "end" | "insideEnd"
