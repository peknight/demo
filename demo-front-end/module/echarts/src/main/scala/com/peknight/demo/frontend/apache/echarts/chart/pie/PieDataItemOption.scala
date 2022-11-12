package com.peknight.demo.frontend.apache.echarts.chart.pie

import com.peknight.demo.frontend.apache.echarts.util.{OptionDataItemObject, OptionDataValueNumeric, StatesOptionMixin}

import scala.scalajs.js

trait PieDataItemOption extends js.Object with OptionDataItemObject[OptionDataValueNumeric] with PieStateOption[_]
  with StatesOptionMixin[PieStateOption[_], ExtraStateOption]:
  val cursor: js.UndefOr[String] = js.undefined
