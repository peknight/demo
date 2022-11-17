package com.peknight.demo.frontend.apache.echarts.coord.geo

import com.peknight.demo.frontend.apache.echarts.util.{BlurScope, CallbackDataParams, StatesEmphasisOptionMixin}

import scala.scalajs.js

trait GeoEmphasisOption extends js.Object with GeoStateOption with StatesEmphasisOptionMixin
object GeoEmphasisOption:
  def apply(itemStyle: js.UndefOr[GeoItemStyleOption[CallbackDataParams]] = js.undefined,
            label: js.UndefOr[GeoLabelOption] = js.undefined,
            blurScope: js.UndefOr[BlurScope] = js.undefined,
            disabled: js.UndefOr[Boolean] = js.undefined): GeoEmphasisOption =
    val _itemStyle: js.UndefOr[GeoItemStyleOption[CallbackDataParams]] = itemStyle
    val _label: js.UndefOr[GeoLabelOption] = label
    val _blurScope: js.UndefOr[BlurScope] = blurScope
    val _disabled: js.UndefOr[Boolean] = disabled
    new GeoEmphasisOption:
      override val itemStyle: js.UndefOr[GeoItemStyleOption[CallbackDataParams]] = _itemStyle
      override val label: js.UndefOr[GeoLabelOption] = _label
      override val blurScope: js.UndefOr[BlurScope] = _blurScope
      override val disabled: js.UndefOr[Boolean] = _disabled

