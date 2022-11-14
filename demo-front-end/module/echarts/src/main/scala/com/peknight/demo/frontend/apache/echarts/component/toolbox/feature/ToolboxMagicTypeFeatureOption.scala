package com.peknight.demo.frontend.apache.echarts.component.toolbox.feature

import com.peknight.demo.frontend.apache.echarts.component.toolbox.ToolboxFeatureOption

import scala.scalajs.js

trait ToolboxMagicTypeFeatureOption extends ToolboxFeatureOption:
  val `type`: js.UndefOr[js.Array[MagicTypeIconType]] = js.undefined
  val seriesIndex: js.UndefOr[SeriesIndex] = js.undefined
