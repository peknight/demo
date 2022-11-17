package com.peknight.demo.frontend.apache.echarts.component.toolbox

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.*

import scala.scalajs.js

trait ToolboxOption[FeatureType <: ToolboxFeatureOption] extends js.Object with ComponentOption
  with BoxLayoutOptionMixin with BorderOptionMixin:
  type Type = String
  type MainType = "toolbox"
  type NameType = OptionName
  val show: js.UndefOr[Boolean] = js.undefined
  val orient: js.UndefOr[LayoutOrient] = js.undefined
  val backgroundColor: js.UndefOr[ZRColor] = js.undefined
  val borderRadius: js.UndefOr[Number | js.Array[Number]] = js.undefined
  val padding: js.UndefOr[Number | js.Array[Number]] = js.undefined
  val itemSize: js.UndefOr[Number] = js.undefined
  val itemGap: js.UndefOr[Number] = js.undefined
  val showTitle: js.UndefOr[Boolean] = js.undefined
  val iconStyle: js.UndefOr[ItemStyleOption[ZRColor]] = js.undefined
  val emphasis: js.UndefOr[IconStyleMixin[ItemStyleOption[CallbackDataParams]]] = js.undefined
  val textStyle: js.UndefOr[LabelOption] = js.undefined
  val tooltip: js.UndefOr[CommonTooltipOption[ToolboxTooltipFormatterParams]] = js.undefined
  /**
   * Write all supported features in the final export option.
   */
  val feature: js.UndefOr[FeatureType] = js.undefined
