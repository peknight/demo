package com.peknight.demo.frontend.apache.echarts.component.toolbox

import com.peknight.demo.frontend.apache.echarts.util.DisplayState

import scala.scalajs.js

trait ToolboxFeatureOption extends js.Object:
  val show: js.UndefOr[Boolean] = js.undefined
  val title: js.UndefOr[String] = js.undefined
  val icon: js.UndefOr[String] = js.undefined
  val iconStyle: js.UndefOr[IconStyle] = js.undefined
  val emphasis: js.UndefOr[IconStyleMixin[IconStyle]] = js.undefined
  val iconStatus: js.UndefOr[DisplayState] = js.undefined
  val onclick: js.UndefOr[js.Function0[Unit]] = js.undefined
