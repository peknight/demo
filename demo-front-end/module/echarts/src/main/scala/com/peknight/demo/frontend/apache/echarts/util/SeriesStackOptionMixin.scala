package com.peknight.demo.frontend.apache.echarts.util

import scala.scalajs.js

trait SeriesStackOptionMixin extends js.Object:
  val stack: js.UndefOr[String] = js.undefined
  val stackStrategy: js.UndefOr["samesign" | "all" | "positive" | "negative"] = js.undefined
