package com.peknight.demo.frontend.apache.echarts.component.toolbox

import com.peknight.demo.frontend.apache.echarts.util.{CallbackDataParams, ItemStyleOption, ZRColor}

import scala.scalajs.js

trait IconStyleMixin[IconStyleType] extends js.Object:
  val iconStyle: js.UndefOr[IconStyleType] = js.undefined
