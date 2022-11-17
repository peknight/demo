package com.peknight.demo.frontend.apache.echarts.coord

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.{ColorString, TextCommonOption}

import scala.scalajs.js

trait AxisLabelBaseOption extends TextCommonOption:
  type ColorType = ColorString | js.Function2[js.UndefOr[String | Number], js.UndefOr[Number], ColorString]
  val show: js.UndefOr[Boolean] = js.undefined
  val inside: js.UndefOr[Boolean] = js.undefined
  val rotate: js.UndefOr[Number] = js.undefined
  val showMinLabel: js.UndefOr[Boolean] = js.undefined
  val showMaxLabel: js.UndefOr[Boolean] = js.undefined
  val margin: js.UndefOr[Number] = js.undefined
  val rich: js.UndefOr[js.Dictionary[TextCommonOption]] = js.undefined
  /**
   * If hide overlapping labels.
   */
  val hideOverlap: js.UndefOr[Boolean] = js.undefined
