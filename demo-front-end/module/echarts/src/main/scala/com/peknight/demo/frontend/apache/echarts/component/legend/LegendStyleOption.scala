package com.peknight.demo.frontend.apache.echarts.component.legend

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.{ColorString, LabelOption}

import scala.scalajs.js

trait LegendStyleOption extends js.Object:
  /**
   * Icon of the legend items.
   *
   * @default "roundRect"
   */
  val icon: js.UndefOr[String] = js.undefined
  /**
   * Color when legend item is not selected
   */
  val inactiveColor: js.UndefOr[ColorString] = js.undefined
  /**
   * Border color when legend item is not selected
   */
  val inactiveBorderColor: js.UndefOr[ColorString] = js.undefined
  /**
   * Border color when legend item is not selected
   */
  val inactiveBorderWidth: js.UndefOr[Number | "auto"] = js.undefined
  /**
   * Legend label formatter
   */
  val formatter: js.UndefOr[String | js.Function1[String, String]] = js.undefined
  val itemStyle: js.UndefOr[LegendItemStyleOption] = js.undefined
  val lineStyle: js.UndefOr[LegendLineStyleOption] = js.undefined
  val textStyle: js.UndefOr[LabelOption] = js.undefined
  val symbolRotate: js.UndefOr[Number | "inherit"] = js.undefined
