package com.peknight.demo.frontend.apache.echarts.chart.helper

import com.peknight.demo.frontend.apache.echarts.util.{DefaultStatesMixinEmphasis, StatesOptionMixin}

import scala.scalajs.js

// TODO
trait LineDrawModelOption extends js.Object with LineDrawStateOption
  with StatesOptionMixin[LineDrawStateOption, DefaultStatesMixinEmphasis, js.Any, js.Any]:
  val effect: js.UndefOr[LineDrawEffectOption] = js.undefined
