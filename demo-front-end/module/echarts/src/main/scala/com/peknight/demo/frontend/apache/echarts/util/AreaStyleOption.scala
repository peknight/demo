package com.peknight.demo.frontend.apache.echarts.util

import com.peknight.demo.frontend.apache.echarts.Number

import scala.scalajs.js

trait AreaStyleOption[Clr] extends ShadowOptionMixin:
  val color: js.UndefOr[Clr] = js.undefined
  val opacity: js.UndefOr[Number] = js.undefined
