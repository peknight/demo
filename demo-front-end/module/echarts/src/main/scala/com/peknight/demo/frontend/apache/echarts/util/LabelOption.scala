package com.peknight.demo.frontend.apache.echarts.util

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.ecomfe.zrender.core.BuiltinTextPosition

import scala.scalajs.js

trait LabelOption extends TextCommonOption:
  type PositionType <: Number | String | js.Array[Number | String] // BuiltinTextPosition | js.Array[Number | String]
  type DistanceType >: Number
  type RotateType >: Number
  type ColorType = String
  /**
   * If show label
   */
  val show: js.UndefOr[Boolean] = js.undefined
  val position: js.UndefOr[PositionType] = js.undefined
  val distance: js.UndefOr[DistanceType] = js.undefined
  val rotate: js.UndefOr[RotateType] = js.undefined
  val offset: js.UndefOr[js.Array[Number]] = js.undefined
  /**
   * Min margin between labels. Used when label has layout.
   */
  val minMargin: js.UndefOr[Number] = js.undefined
  val overflow: js.UndefOr["break" | "breakAll" | "truncate" | "none"] = js.undefined
  val silent: js.UndefOr[Boolean] = js.undefined
  val precision: js.UndefOr[Number | "auto"] = js.undefined
  val valueAnimation: js.UndefOr[Boolean] = js.undefined
  val rich: js.UndefOr[TextCommonOption] = js.undefined

object LabelOption:
  def apply(color: js.UndefOr[String] = js.undefined,
            fontStyle: js.UndefOr[ZRFontStyle] = js.undefined,
            fontWeight: js.UndefOr[ZRFontWeight] = js.undefined,
            fontFamily: js.UndefOr[String] = js.undefined,
            fontSize: js.UndefOr[Number | String] = js.undefined,
            shadowBlur: js.UndefOr[Number] = js.undefined,
            shadowColor: js.UndefOr[ColorString] = js.undefined,
            shadowOffsetX: js.UndefOr[Number] = js.undefined,
            shadowOffsetY: js.UndefOr[Number] = js.undefined,
            align: js.UndefOr[HorizontalAlign] = js.undefined,
            verticalAlign: js.UndefOr[VerticalAlign] = js.undefined,
            baseline: js.UndefOr[VerticalAlign] = js.undefined,
            opacity: js.UndefOr[Number] = js.undefined,
            lineHeight: js.UndefOr[Number] = js.undefined,
            backgroundColor: js.UndefOr[ColorString | ImageMixin] = js.undefined,
            borderColor: js.UndefOr[String] = js.undefined,
            borderWidth: js.UndefOr[Number] = js.undefined,
            borderType: js.UndefOr[ZRLineType] = js.undefined,
            borderDashOffset: js.UndefOr[Number] = js.undefined,
            borderRadius: js.UndefOr[Number | js.Array[Number]] = js.undefined,
            padding: js.UndefOr[Number | js.Array[Number]] = js.undefined,
            width: js.UndefOr[Number | String] = js.undefined,
            height: js.UndefOr[Number] = js.undefined,
            textBorderColor: js.UndefOr[String] = js.undefined,
            textBorderWidth: js.UndefOr[Number] = js.undefined,
            textBorderType: js.UndefOr[ZRLineType] = js.undefined,
            textBorderDashOffset: js.UndefOr[Number] = js.undefined,
            textShadowBlur: js.UndefOr[Number] = js.undefined,
            textShadowColor: js.UndefOr[String] = js.undefined,
            textShadowOffsetX: js.UndefOr[Number] = js.undefined,
            textShadowOffsetY: js.UndefOr[Number] = js.undefined,
            tag: js.UndefOr[String] = js.undefined,
            show: js.UndefOr[Boolean] = js.undefined,
            position: js.UndefOr[BuiltinTextPosition | js.Array[Number | String]] = js.undefined,
            distance: js.UndefOr[Number] = js.undefined,
            rotate: js.UndefOr[Number] = js.undefined,
            offset: js.UndefOr[js.Array[Number]] = js.undefined,
            minMargin: js.UndefOr[Number] = js.undefined,
            overflow: js.UndefOr["break" | "breakAll" | "truncate" | "none"] = js.undefined,
            silent: js.UndefOr[Boolean] = js.undefined,
            precision: js.UndefOr[Number | "auto"] = js.undefined,
            valueAnimation: js.UndefOr[Boolean] = js.undefined,
            rich: js.UndefOr[TextCommonOption] = js.undefined): LabelOption =
    val _color: js.UndefOr[String] = color
    val _fontStyle: js.UndefOr[ZRFontStyle] = fontStyle
    val _fontWeight: js.UndefOr[ZRFontWeight] = fontWeight
    val _fontFamily: js.UndefOr[String] = fontFamily
    val _fontSize: js.UndefOr[Number | String] = fontSize
    val _shadowBlur: js.UndefOr[Number] = shadowBlur
    val _shadowColor: js.UndefOr[ColorString] = shadowColor
    val _shadowOffsetX: js.UndefOr[Number] = shadowOffsetX
    val _shadowOffsetY: js.UndefOr[Number] = shadowOffsetY
    val _align: js.UndefOr[HorizontalAlign] = align
    val _verticalAlign: js.UndefOr[VerticalAlign] = verticalAlign
    val _baseline: js.UndefOr[VerticalAlign] = baseline
    val _opacity: js.UndefOr[Number] = opacity
    val _lineHeight: js.UndefOr[Number] = lineHeight
    val _backgroundColor: js.UndefOr[ColorString | ImageMixin] = backgroundColor
    val _borderColor: js.UndefOr[String] = borderColor
    val _borderWidth: js.UndefOr[Number] = borderWidth
    val _borderType: js.UndefOr[ZRLineType] = borderType
    val _borderDashOffset: js.UndefOr[Number] = borderDashOffset
    val _borderRadius: js.UndefOr[Number | js.Array[Number]] = borderRadius
    val _padding: js.UndefOr[Number | js.Array[Number]] = padding
    val _width: js.UndefOr[Number | String] = width
    val _height: js.UndefOr[Number] = height
    val _textBorderColor: js.UndefOr[String] = textBorderColor
    val _textBorderWidth: js.UndefOr[Number] = textBorderWidth
    val _textBorderType: js.UndefOr[ZRLineType] = textBorderType
    val _textBorderDashOffset: js.UndefOr[Number] = textBorderDashOffset
    val _textShadowBlur: js.UndefOr[Number] = textShadowBlur
    val _textShadowColor: js.UndefOr[String] = textShadowColor
    val _textShadowOffsetX: js.UndefOr[Number] = textShadowOffsetX
    val _textShadowOffsetY: js.UndefOr[Number] = textShadowOffsetY
    val _tag: js.UndefOr[String] = tag
    val _show: js.UndefOr[Boolean] = show
    val _position: js.UndefOr[BuiltinTextPosition | js.Array[Number | String]] = position
    val _distance: js.UndefOr[Number] = distance
    val _rotate: js.UndefOr[Number] = rotate
    val _offset: js.UndefOr[js.Array[Number]] = offset
    val _minMargin: js.UndefOr[Number] = minMargin
    val _overflow: js.UndefOr["break" | "breakAll" | "truncate" | "none"] = overflow
    val _silent: js.UndefOr[Boolean] = silent
    val _precision: js.UndefOr[Number | "auto"] = precision
    val _valueAnimation: js.UndefOr[Boolean] = valueAnimation
    val _rich: js.UndefOr[TextCommonOption] = rich
    new LabelOption:
      type PositionType = BuiltinTextPosition | js.Array[Number | String]
      type DistanceType = Number
      type RotateType = Number
      override val color: js.UndefOr[String] = _color
      override val fontStyle: js.UndefOr[ZRFontStyle] = _fontStyle
      override val fontWeight: js.UndefOr[ZRFontWeight] = _fontWeight
      override val fontFamily: js.UndefOr[String] = _fontFamily
      override val fontSize: js.UndefOr[Number | String] = _fontSize
      override val shadowBlur: js.UndefOr[Number] = _shadowBlur
      override val shadowColor: js.UndefOr[ColorString] = _shadowColor
      override val shadowOffsetX: js.UndefOr[Number] = _shadowOffsetX
      override val shadowOffsetY: js.UndefOr[Number] = _shadowOffsetY
      override val align: js.UndefOr[HorizontalAlign] = _align
      override val verticalAlign: js.UndefOr[VerticalAlign] = _verticalAlign
      override val baseline: js.UndefOr[VerticalAlign] = _baseline
      override val opacity: js.UndefOr[Number] = _opacity
      override val lineHeight: js.UndefOr[Number] = _lineHeight
      override val backgroundColor: js.UndefOr[ColorString | ImageMixin] = _backgroundColor
      override val borderColor: js.UndefOr[String] = _borderColor
      override val borderWidth: js.UndefOr[Number] = _borderWidth
      override val borderType: js.UndefOr[ZRLineType] = _borderType
      override val borderDashOffset: js.UndefOr[Number] = _borderDashOffset
      override val borderRadius: js.UndefOr[Number | js.Array[Number]] = _borderRadius
      override val padding: js.UndefOr[Number | js.Array[Number]] = _padding
      override val width: js.UndefOr[Number | String] = _width
      override val height: js.UndefOr[Number] = _height
      override val textBorderColor: js.UndefOr[String] = _textBorderColor
      override val textBorderWidth: js.UndefOr[Number] = _textBorderWidth
      override val textBorderType: js.UndefOr[ZRLineType] = _textBorderType
      override val textBorderDashOffset: js.UndefOr[Number] = _textBorderDashOffset
      override val textShadowBlur: js.UndefOr[Number] = _textShadowBlur
      override val textShadowColor: js.UndefOr[String] = _textShadowColor
      override val textShadowOffsetX: js.UndefOr[Number] = _textShadowOffsetX
      override val textShadowOffsetY: js.UndefOr[Number] = _textShadowOffsetY
      override val tag: js.UndefOr[String] = _tag
      override val show: js.UndefOr[Boolean] = _show
      override val position: js.UndefOr[PositionType] = _position
      override val distance: js.UndefOr[Number] = _distance
      override val rotate: js.UndefOr[RotateType] = _rotate
      override val offset: js.UndefOr[js.Array[Number]] = _offset
      override val minMargin: js.UndefOr[Number] = _minMargin
      override val overflow: js.UndefOr["break" | "breakAll" | "truncate" | "none"] = _overflow
      override val silent: js.UndefOr[Boolean] = _silent
      override val precision: js.UndefOr[Number | "auto"] = _precision
      override val valueAnimation: js.UndefOr[Boolean] = _valueAnimation
      override val rich: js.UndefOr[TextCommonOption] = _rich
