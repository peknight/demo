package com.peknight.demo.frontend.apache.echarts.util

import scala.scalajs.js

// TODO
trait SeriesEncodeOptionMixin extends js.Object:
  val datasetIndex: js.UndefOr[Number] = js.undefined
  val datasetId: js.UndefOr[String | Number] = js.undefined
  val seriesLayoutBy: js.UndefOr[SeriesLayoutBy] = js.undefined
  val sourceHeader: js.UndefOr[OptionSourceHeader] = js.undefined
  val dimensions: js.UndefOr[js.Array[DimensionDefinitionLoose]] = js.undefined
  val encode: js.UndefOr[OptionEncode] = js.undefined
