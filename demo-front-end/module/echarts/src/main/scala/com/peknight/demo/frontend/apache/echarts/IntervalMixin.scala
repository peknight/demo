package com.peknight.demo.frontend.apache.echarts

import scala.scalajs.js

trait IntervalMixin extends js.Object:
  val interval: js.UndefOr["auto" | Number | js.Function2[Number, String, Boolean]] = js.undefined
