package com.peknight.demo.frontend.apache.echarts.coord

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.*

import scala.scalajs.js

trait AxisLabelOption[TType <: OptionAxisType] extends AxisLabelBaseOption:
  val formatter: js.UndefOr[LabelFormatters[TType]] = js.undefined

object AxisLabelOption:
  def apply[TType <: OptionAxisType](
            color: js.UndefOr[ColorString | js.Function2[js.UndefOr[String | Number], js.UndefOr[Number], ColorString]] = js.undefined,
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
            inside: js.UndefOr[Boolean] = js.undefined,
            rotate: js.UndefOr[Number] = js.undefined,
            showMinLabel: js.UndefOr[Boolean] = js.undefined,
            showMaxLabel: js.UndefOr[Boolean] = js.undefined,
            margin: js.UndefOr[Number] = js.undefined,
            rich: js.UndefOr[js.Dictionary[TextCommonOption]] = js.undefined,
            hideOverlap: js.UndefOr[Boolean] = js.undefined,
            formatter: js.UndefOr[LabelFormatters[TType]] = js.undefined,
                                    ): AxisLabelOption[TType] & IntervalMixin =
    val _color: js.UndefOr[ColorString | js.Function2[js.UndefOr[String | Number], js.UndefOr[Number], ColorString]] = color
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
    val _inside: js.UndefOr[Boolean] = inside
    val _rotate: js.UndefOr[Number] = rotate
    val _showMinLabel: js.UndefOr[Boolean] = showMinLabel
    val _showMaxLabel: js.UndefOr[Boolean] = showMaxLabel
    val _margin: js.UndefOr[Number] = margin
    val _rich: js.UndefOr[js.Dictionary[TextCommonOption]] = rich
    val _hideOverlap: js.UndefOr[Boolean] = hideOverlap
    val _formatter: js.UndefOr[LabelFormatters[TType]] = formatter
    new AxisLabelOption[TType] with IntervalMixin:
      override val color: js.UndefOr[ColorType] = _color
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
      override val inside: js.UndefOr[Boolean] = _inside
      override val rotate: js.UndefOr[Number] = _rotate
      override val showMinLabel: js.UndefOr[Boolean] = _showMinLabel
      override val showMaxLabel: js.UndefOr[Boolean] = _showMaxLabel
      override val margin: js.UndefOr[Number] = _margin
      override val rich: js.UndefOr[js.Dictionary[TextCommonOption]] = _rich
      override val hideOverlap: js.UndefOr[Boolean] = _hideOverlap
      override val formatter: js.UndefOr[LabelFormatters[TType]] = _formatter

