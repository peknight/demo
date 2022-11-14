package com.peknight.demo.frontend.apache.echarts.component.toolbox.feature

import com.peknight.demo.frontend.apache.echarts.component.toolbox.ToolboxFeatureOption

import scala.scalajs.js

// TODO
trait ToolboxBrushFeatureOption extends ToolboxFeatureOption:
  val `type`: js.UndefOr[js.Array[BrushIconType]] = js.undefined
