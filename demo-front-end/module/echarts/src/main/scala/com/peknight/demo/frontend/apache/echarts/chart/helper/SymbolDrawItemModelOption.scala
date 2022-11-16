package com.peknight.demo.frontend.apache.echarts.chart.helper

import com.peknight.demo.frontend.apache.echarts.util.{StatesOptionMixin, SymbolOptionMixin}

import scala.scalajs.js

trait SymbolDrawItemModelOption extends js.Object with SymbolOptionMixin[js.Object]
  with StatesOptionMixin[SymbolDrawStateOption, DefaultEmphasisFocusScaleMixin, js.Any, js.Any]
  with SymbolDrawStateOption:
  val cursor: js.UndefOr[String] = js.undefined
  val rippleEffect: js.UndefOr[RippleEffectOption] = js.undefined
