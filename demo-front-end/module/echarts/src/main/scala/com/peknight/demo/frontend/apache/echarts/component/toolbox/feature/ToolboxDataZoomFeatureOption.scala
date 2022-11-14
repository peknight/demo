package com.peknight.demo.frontend.apache.echarts.component.toolbox.feature

import com.peknight.demo.frontend.apache.echarts.component.toolbox.ToolboxFeatureOption
import com.peknight.demo.frontend.apache.echarts.util.{CallbackDataParams, ItemStyleOption, ModelFinderIdQuery, ModelFinderIndexQuery}

import scala.scalajs.js

trait ToolboxDataZoomFeatureOption extends ToolboxFeatureOption:
  val `type`: js.UndefOr[js.Array[DataZoomIconType]] = js.undefined
  val filterMode: js.UndefOr["filter" | "weakFilter" | "empty" | "none"] = js.undefined
  val xAxisIndex: js.UndefOr[ModelFinderIndexQuery] = js.undefined
  val yAxisIndex: js.UndefOr[ModelFinderIndexQuery] = js.undefined
  val xAxisId: js.UndefOr[ModelFinderIdQuery] = js.undefined
  val yAxisId: js.UndefOr[ModelFinderIdQuery] = js.undefined
  val brushStyle: js.UndefOr[ItemStyleOption[CallbackDataParams]] = js.undefined
