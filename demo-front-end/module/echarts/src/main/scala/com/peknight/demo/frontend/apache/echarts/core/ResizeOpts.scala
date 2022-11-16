package com.peknight.demo.frontend.apache.echarts.core

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.AnimationOption

import scala.scalajs.js

trait ResizeOpts extends js.Object:
  val width: js.UndefOr[Number | "auto"] = js.undefined
  val height: js.UndefOr[Number | "auto"] = js.undefined
  val animation: js.UndefOr[AnimationOption] = js.undefined
  val silent: js.UndefOr[Boolean] = js.undefined
