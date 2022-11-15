package com.peknight.demo.frontend.apache.echarts.util

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.ecomfe.zrender.core.BuiltinTextPosition

import scala.scalajs.js

trait LabelOption extends TextCommonOption:
  type PositionType >: BuiltinTextPosition | js.Array[Number | String]
  type RotateType >: Number
  /**
   * If show label
   */
  val show: js.UndefOr[Boolean] = js.undefined
  val position: js.UndefOr[PositionType] = js.undefined
  val distance: js.UndefOr[Number] = js.undefined
  val rotate: js.UndefOr[RotateType] = js.undefined
  val offset: js.UndefOr[js.Array[Number]] = js.undefined
  /**
   * Min margin between labels. Used when label has layout.
   */
  val minMargin: js.UndefOr[Number] = js.undefined
  val overflow: js.UndefOr["break" | "breakAll" | "truncate" | "none"] = js.undefined
  val silent: js.UndefOr[Boolean] = js.undefined
  val precision: js.UndefOr[Number | "auto"] = js.undefined
  val valueAnimation: js.UndefOr[Boolean] = js.undefined
  val rich: js.UndefOr[js.Dictionary[TextCommonOption]] = js.undefined
