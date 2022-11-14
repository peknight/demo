package com.peknight.demo.frontend.apache.echarts.util

import scala.scalajs.js

trait SeriesSamplingOptionMixin extends js.Object:
  val sampling: js.UndefOr["none" | "average" | "min" | "max" | "sum" | "lttb" | SamplingFunc] = js.undefined
