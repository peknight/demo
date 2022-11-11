package com.peknight.demo.frontend.apache.echarts.chart.pie

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.ItemStyleOption

import scala.scalajs.js

trait PieItemStyleOption[TCbParams] extends ItemStyleOption[TCbParams]:
  val borderRadius: js.UndefOr[js.Array[Number | String] | Number | String] = js.undefined
