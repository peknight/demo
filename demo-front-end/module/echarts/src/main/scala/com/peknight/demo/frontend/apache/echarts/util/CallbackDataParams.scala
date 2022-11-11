package com.peknight.demo.frontend.apache.echarts.util

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.data.helper.DimensionUserOuputEncode

import scala.scalajs.js

@js.native
trait CallbackDataParams extends js.Object:
  val componentType: String = js.native
  val componentSubType: String = js.native
  val componentIndex: Number = js.native
  val seriesType: js.UndefOr[String] = js.native
  val seriesIndex: js.UndefOr[Number] = js.native
  val seriesId: js.UndefOr[String] = js.native
  val seriesName: js.UndefOr[String] = js.native
  val name: String = js.native
  val dataIndex: Number = js.native
  val data: OptionDataItem = js.native
  val dataType: js.UndefOr[SeriesDataType] = js.native
  val value: OptionDataItem | OptionDataValue = js.native
  val color: js.UndefOr[ZRColor] = js.native
  val borderColor: js.UndefOr[String] = js.native
  val dimensionNames: js.UndefOr[js.Array[DimensionName]] = js.native
  val encode: js.UndefOr[DimensionUserOuputEncode] = js.native
  val marker: js.UndefOr[TooltipMarker] = js.native
  val status: js.UndefOr[DisplayState] = js.native
  val dimensionIndex: js.UndefOr[Number] = js.native
  val percent: js.UndefOr[Number] = js.native
  val $vars: js.Array[String] = js.native
