package com.peknight.demo.frontend.apache.echarts.util

import scala.scalajs.js

trait StatesEmphasisOptionMixin extends js.Object:
  /**
   * Scope of blurred element when focus.
   *
   * coordinateSystem: blur others in the same coordinateSystem
   * series: blur others in the same series
   * global: blur all others
   *
   * Default to be coordinate system.
   */
  val blurScope: js.UndefOr[BlurScope] = js.undefined
  /**
   * If emphasis state is disabled.
   */
  val disabled: js.UndefOr[Boolean] = js.undefined
