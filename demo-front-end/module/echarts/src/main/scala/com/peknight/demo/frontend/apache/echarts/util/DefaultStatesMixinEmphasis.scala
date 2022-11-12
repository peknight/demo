package com.peknight.demo.frontend.apache.echarts.util

import scala.scalajs.js

trait DefaultStatesMixinEmphasis extends js.Object:
  /**
   * self: Focus self and blur all others.
   * series: Focus series and blur all other series.
   */
  val focus: js.UndefOr[DefaultEmphasisFocus] = js.undefined
