package com.peknight.demo.frontend.apache.echarts.util

import scala.scalajs.js

trait SeriesLabelOption extends LabelOption:
  val formatter: js.UndefOr[String | LabelFormatterCallback[CallbackDataParams]] = js.undefined
