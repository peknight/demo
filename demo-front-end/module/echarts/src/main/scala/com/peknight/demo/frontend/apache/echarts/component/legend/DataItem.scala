package com.peknight.demo.frontend.apache.echarts.component.legend

import scala.scalajs.js

trait DataItem extends LegendStyleOption:
  type TooltipType
  val name: js.UndefOr[String] = js.undefined
  val tooltip: js.UndefOr[TooltipType] = js.undefined
