package com.peknight.demo.frontend.apache.echarts.util

import scala.scalajs.js

trait StatesOptionMixin[StateOption, EmphasisType, SelectType, BlurType] extends js.Object:
  /**
   * Emphasis states
   */
  val emphasis: js.UndefOr[StateOption & EmphasisType & StatesEmphasisOptionMixin] = js.undefined
  /**
   * Select states
   */
  val select: js.UndefOr[StateOption & SelectType & StatesSelectOptionMixin] = js.undefined
  /**
   * Blur states.
   */
  val blur: js.UndefOr[StateOption & BlurType] = js.undefined
