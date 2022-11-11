package com.peknight.demo.frontend.apache.echarts.coord

import com.peknight.demo.frontend.apache.echarts.util.TextCommonOption
import scala.scalajs.js

trait AxisNameTextStyleOption extends TextCommonOption:
  val rich: js.UndefOr[js.Dictionary[TextCommonOption]]
