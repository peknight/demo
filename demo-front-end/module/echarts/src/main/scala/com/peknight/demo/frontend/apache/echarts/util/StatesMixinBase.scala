package com.peknight.demo.frontend.apache.echarts.util

import scala.scalajs.js

trait StatesMixinBase extends js.Object:
  type EmphasisType
  type SelectType
  type BlurType
  val emphasis: js.UndefOr[EmphasisType] = js.undefined
  val select: js.UndefOr[SelectType] = js.undefined
  val blur: js.UndefOr[BlurType] = js.undefined