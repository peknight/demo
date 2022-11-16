package com.peknight.demo.frontend.apache.echarts.util

import scala.scalajs.js

trait SeriesLineLabelOption extends LineLabelOption:
  val formatter: js.UndefOr[String | LabelFormatterCallback[CallbackDataParams]] = js.undefined
  
