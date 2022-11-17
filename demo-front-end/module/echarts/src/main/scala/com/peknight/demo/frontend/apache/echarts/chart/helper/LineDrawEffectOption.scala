package com.peknight.demo.frontend.apache.echarts.chart.helper

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.ColorString

import scala.scalajs.js

trait LineDrawEffectOption extends js.Object:
  val show: js.UndefOr[Boolean] = js.undefined
  val period: js.UndefOr[Number] = js.undefined
  val delay: js.UndefOr[Number | js.Function1[Number, Number]] = js.undefined
  /**
   * If move with constant speed px/sec
   * period will be ignored if this property is > 0,
   */
  val constantSpeed: js.UndefOr[Number] = js.undefined
  val symbol: js.UndefOr[String] = js.undefined
  val symbolSize: js.UndefOr[Number | js.Array[Number]] = js.undefined
  val loop: js.UndefOr[Boolean] = js.undefined
  val roundTrip: js.UndefOr[Boolean] = js.undefined
  /**
   * Length of trail, 0 - 1
   */
  val trailLength: js.UndefOr[Number] = js.undefined
  /**
   * Default to be same with lineStyle.color
   */
  val color: js.UndefOr[ColorString] = js.undefined

object LineDrawEffectOption:
  def apply(show: js.UndefOr[Boolean] = js.undefined,
            period: js.UndefOr[Number] = js.undefined,
            delay: js.UndefOr[Number | js.Function1[Number, Number]] = js.undefined,
            constantSpeed: js.UndefOr[Number] = js.undefined,
            symbol: js.UndefOr[String] = js.undefined,
            symbolSize: js.UndefOr[Number | js.Array[Number]] = js.undefined,
            loop: js.UndefOr[Boolean] = js.undefined,
            roundTrip: js.UndefOr[Boolean] = js.undefined,
            trailLength: js.UndefOr[Number] = js.undefined,
            color: js.UndefOr[ColorString] = js.undefined): LineDrawEffectOption =
    val _show: js.UndefOr[Boolean] = show
    val _period: js.UndefOr[Number] = period
    val _delay: js.UndefOr[Number | js.Function1[Number, Number]] = delay
    val _constantSpeed: js.UndefOr[Number] = constantSpeed
    val _symbol: js.UndefOr[String] = symbol
    val _symbolSize: js.UndefOr[Number | js.Array[Number]] = symbolSize
    val _loop: js.UndefOr[Boolean] = loop
    val _roundTrip: js.UndefOr[Boolean] = roundTrip
    val _trailLength: js.UndefOr[Number] = trailLength
    val _color: js.UndefOr[ColorString] = color
    new LineDrawEffectOption:
      override val show: js.UndefOr[Boolean] = _show
      override val period: js.UndefOr[Number] = _period
      override val delay: js.UndefOr[Number | js.Function1[Number, Number]] = _delay
      override val constantSpeed: js.UndefOr[Number] = _constantSpeed
      override val symbol: js.UndefOr[String] = _symbol
      override val symbolSize: js.UndefOr[Number | js.Array[Number]] = _symbolSize
      override val loop: js.UndefOr[Boolean] = _loop
      override val roundTrip: js.UndefOr[Boolean] = _roundTrip
      override val trailLength: js.UndefOr[Number] = _trailLength
      override val color: js.UndefOr[ColorString] = _color
