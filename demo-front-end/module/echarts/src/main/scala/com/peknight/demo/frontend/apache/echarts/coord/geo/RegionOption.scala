package com.peknight.demo.frontend.apache.echarts.coord.geo

import com.peknight.demo.frontend.apache.echarts.util.{CommonTooltipOption, StatesOptionMixin}

import scala.scalajs.js

trait RegionOption extends js.Object with GeoStateOption with StatesOptionMixin[GeoStateOption, js.Any, js.Any, js.Any]:
  val name: js.UndefOr[String] = js.undefined
  val selected: js.UndefOr[Boolean] = js.undefined
  val tooltip: js.UndefOr[CommonTooltipOption[GeoTooltipFormatterParams]] = js.undefined
