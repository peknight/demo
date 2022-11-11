package com.peknight.demo.frontend.apache.echarts.`export`

import com.peknight.demo.frontend.apache.echarts.component.marker.{MarkAreaOption, MarkLineOption, MarkPointOption}
import com.peknight.demo.frontend.apache.echarts.util.SeriesTooltipOption

import scala.scalajs.js

trait SeriesInjectedOption extends js.Object:
  val markArea: js.UndefOr[MarkAreaOption] = js.undefined
  val markLine: js.UndefOr[MarkLineOption] = js.undefined
  val markPoint: js.UndefOr[MarkPointOption] = js.undefined
  val tooltip: js.UndefOr[SeriesTooltipOption] = js.undefined
