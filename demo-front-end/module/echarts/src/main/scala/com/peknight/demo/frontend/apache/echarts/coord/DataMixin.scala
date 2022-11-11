package com.peknight.demo.frontend.apache.echarts.coord

import com.peknight.demo.frontend.apache.echarts.util.{OrdinalRawValue, TextCommonOption}

import scala.scalajs.js

@js.native
trait DataMixin extends js.Object:
  val value: OrdinalRawValue = js.native
  val textStyle: js.UndefOr[TextCommonOption] = js.native
