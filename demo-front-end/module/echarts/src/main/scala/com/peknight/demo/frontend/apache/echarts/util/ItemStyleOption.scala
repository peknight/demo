package com.peknight.demo.frontend.apache.echarts.util

import com.peknight.demo.frontend.apache.echarts.Number

import scala.scalajs.js

trait ItemStyleOption[TCbParams] extends js.Object with ShadowOptionMixin with BorderOptionMixin:
  val color: js.UndefOr[ZRColor | TCbParams | js.Function1[TCbParams, ZRColor]] = js.undefined
  val opacity: js.UndefOr[Number] = js.undefined
  val decal: js.UndefOr[DecalObject | "none"] = js.undefined
