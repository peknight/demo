package com.peknight.demo.frontend.apache.echarts.util

import com.peknight.demo.frontend.apache.echarts.data.DataStoreDimensionType

import scala.scalajs.js

trait DimensionDefinition extends js.Object:
  val `type`: js.UndefOr[DataStoreDimensionType] = js.undefined
  val name: js.UndefOr[DimensionName] = js.undefined
  val displayName: js.UndefOr[String] = js.undefined
