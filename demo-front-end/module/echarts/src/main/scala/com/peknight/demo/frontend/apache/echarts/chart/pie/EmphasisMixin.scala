package com.peknight.demo.frontend.apache.echarts.chart.pie

import com.peknight.demo.frontend.apache.echarts.util.DefaultEmphasisFocus

import scala.scalajs.js

trait EmphasisMixin extends js.Object:
  val focus: js.UndefOr[DefaultEmphasisFocus] = js.undefined
  val scale: js.UndefOr[Boolean] = js.undefined
  val scaleSize: js.UndefOr[Number] = js.undefined
  val itemStyle: js.UndefOr[PieItemStyleOption[_]] = js.undefined

object EmphasisMixin:
  def apply(itemStyle: js.UndefOr[PieItemStyleOption[_]] = js.undefined): EmphasisMixin =
    val _itemStyle: js.UndefOr[PieItemStyleOption[_]] = itemStyle
    new EmphasisMixin:
      override val itemStyle: js.UndefOr[PieItemStyleOption[_]] = _itemStyle

