package com.peknight.demo.frontend.apache.echarts.util

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.ecomfe.zrender.core.BuiltinTextPosition

import scala.scalajs.js

trait LineLabelOption extends LabelOption:
  type PositionType = "start" | "middle" | "end" | "insideStart" | "insideStartTop" | "insideStartBottom" |
    "insideMiddle" | "insideMiddleTop" | "insideMiddleBottom" | "insideEnd" | "insideEndTop" | "insideEndBottom" |
    "insideMiddleBottom"
  type DistanceType = Number | js.Array[Number]
  type RotateType = Number
