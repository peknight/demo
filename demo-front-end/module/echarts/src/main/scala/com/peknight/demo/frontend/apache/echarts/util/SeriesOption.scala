package com.peknight.demo.frontend.apache.echarts.util

import com.peknight.demo.frontend.apache.echarts.Number

import scala.scalajs.js

trait SeriesOption[StateOption, StatesMixin <: StatesMixinBase] extends js.Object with ComponentOption
  with AnimationOptionMixin with ColorPaletteOptionMixin with StatesOptionMixin[StateOption, StatesMixin]:
  type MainType = "series"
  type NameType = OptionName
  type CoordinateSystemType <: String
  type DataType

  val silent: js.UndefOr[Boolean] = js.undefined
  val blendMode: js.UndefOr[String] = js.undefined
  /**
   * Cursor when mouse on the elements
   */
  val cursor: js.UndefOr[String] = js.undefined
  /**
   * groupId of data. can be used for doing drilldown / up animation
   * It will be ignored if:
   *  - groupId is specified in each data
   *  - encode.itemGroupId is given.
   */
  val dataGroupId: js.UndefOr[OptionId] = js.undefined
  val data: js.UndefOr[DataType] = js.undefined
  val colorBy: js.UndefOr[ColorBy] = js.undefined
  val legendHoverLink: js.UndefOr[Boolean] = js.undefined
  /**
   * Configurations about progressive rendering
   */
  val progressive: js.UndefOr[Number | false] = js.undefined
  val progressiveThreshold: js.UndefOr[Number] = js.undefined
  val progressiveChunkMode: js.UndefOr["mod"] = js.undefined
  /**
   * Not available on every series
   */
  val coordinateSystem: js.UndefOr[CoordinateSystemType] = js.undefined
  val hoverLayerThreshold: js.UndefOr[Number] = js.undefined
  /**
   * When dataset is used, seriesLayoutBy specifies whether the column or the row of dataset is mapped to the series
   * namely, the series is "layout" on columns or rows
   * @default "column"
   */
  val seriesLayoutBy: js.UndefOr[SeriesLayoutBy] = js.undefined
  val labelLine: js.UndefOr[LabelLineOption] = js.undefined
  /**
   * Overall label layout option in label layout stage.
   */
  val labelLayout: js.UndefOr[LabelLayoutOption | LabelLayoutOptionCallback] = js.undefined
  /**
   * Animation config for state transition.
   */
  val stateAnimation: js.UndefOr[AnimationOption] = js.undefined
  /**
   * If enabled universal transition cross series.
   * @example
   *  universalTransition: true
   *  universalTransition: { enabled: true }
   */
  val universalTransition: js.UndefOr[Boolean | UniversalTransitionOption] = js.undefined
  /**
   * Map of selected data
   * key is name or index of data.
   */
  val selectedMap: js.UndefOr[js.Dictionary[Boolean] | "all"] = js.undefined
  val selectedMode: js.UndefOr["single" | "multiple" | "series" | Boolean] = js.undefined

