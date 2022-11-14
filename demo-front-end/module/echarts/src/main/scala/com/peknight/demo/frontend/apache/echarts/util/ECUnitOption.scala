package com.peknight.demo.frontend.apache.echarts.util

import scala.scalajs.js

trait ECUnitOption extends js.Object with AnimationOptionMixin with ColorPaletteOptionMixin:
  type BaseOptionType
  val baseOption: js.UndefOr[BaseOptionType] = js.undefined
  type OptionsType
  val options: js.UndefOr[OptionsType] = js.undefined
  type MediaType
  val media: js.UndefOr[MediaType] = js.undefined
  type TimelineType <: ComponentOption | js.Array[ComponentOption]
  val timeline: js.UndefOr[TimelineType] = js.undefined
  val backgroundColor: js.UndefOr[ZRColor] = js.undefined
  val darkMode: js.UndefOr[Boolean | "auto"] = js.undefined
  val textStyle: js.UndefOr[BaseTextStyle] = js.undefined
  val useUTC: js.UndefOr[Boolean] = js.undefined
  val stateAnimation: js.UndefOr[AnimationOption] = js.undefined
